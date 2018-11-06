package cn.merson.examination.common.util;

import cn.merson.examination.configuration.BaseExaminationApplicationContext;
import cn.merson.examination.entity.Question;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Redis缓存工具类  redis中缓存题库 试卷数据
 */
@Component
public class RedisUtil extends BaseExaminationApplicationContext{

    @Autowired
    private StringUtil stringUtil;

    //单位是秒，默认的缓存超时时间为1分钟(测试)
    private int timeout = 60;

    /**
     * 获取Jedis资源
     * @return
     */
    public Jedis getJedis(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(30);
        //可以写到配置文件中
        JedisPool jedisPool = new JedisPool(config,"127.0.0.1",6379);
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis;
        }catch (JedisConnectionException e) {
            e.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭Jedis
     * @param jedis
     */
    private void closeResource(Jedis jedis){
        if (jedis != null){
            jedis.close();
        }
    }


    /**
     * 判断指定的Key是否已经在缓存中
     * @param key
     * @return
     */
    public boolean isKeyCache(String key){
        if (stringUtil.isNullOrEmpty(key)){
            return false;
        }
        Jedis jedis = getJedis();
        boolean isExist = jedis.exists(key);
        closeResource(jedis);
        return isExist;
    }

    /**
     * 清空所有缓存
     */
    public void flushAll(){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.flushAll();
        }catch (Exception ex){
            closeResource(jedis);
        } finally {
            closeResource(jedis);
        }
    }

    /**
     * 清除指定key的缓存
     * @param key
     */
    public void flushByKey(String key){
        Jedis jedis = getJedis();
        if (stringUtil.isNullOrEmpty(key) || !jedis.exists(key.getBytes())){
            closeResource(jedis);
            return;
        }
        jedis.del(key.getBytes());
        closeResource(jedis);
    }


    /**
     *   缓存List<Object>
     * @param key
     * @param list
     * @param <T>
     */
    public  <T> void cacheList(String key,List<T> list){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            //如果已经缓存过则清除
            flushByKey(key);
            String result = jedis.setex(key.getBytes(), timeout, serialize1(list));
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            closeResource(jedis);
        }
    }

    /**
     * 缓存Object
     * @param key
     * @param object
     */
    public void cacheObject(String key,Object object){
        Jedis jedis = getJedis();
        try {
            String result = jedis.setex(key.getBytes(), timeout, serialize1(object));
            System.out.println(result);
            //jedis.set(key.getBytes(),serialize(object));
        } catch (Exception e) {
            //logger.error("Set key error : "+e);
        } finally {
            closeResource(jedis);
        }
    }

    /**
     * 获取缓存的集合
     * @param key
     * @param <T>
     * @return
     */
    public  <T> List<T> getList(String key){
        //String bKey = buildKey(key);
        Jedis jedis = getJedis();
        if(jedis == null || !jedis.exists(key.getBytes())){
            return null;
        }
        try
        {
            byte[] bytes = jedis.get(key.getBytes());
            List<T> list = (List<T>) deserialize1(bytes);
            return list;
        }
        catch (Exception e) {
        } finally {
            closeResource(jedis);
        }
        return null;
    }

    /**
     * 获取缓存的Object对象
     * @param key
     * @return
     */
    public Object getObject(String key){
        Jedis jedis = getJedis();
        if(jedis == null || !jedis.exists(key.getBytes())){
            return null;
        }
        try
        {
            byte[] bytes = jedis.get(key.getBytes());
            Object object = (Object) deserialize1(bytes);
            return object;
        }
        catch (Exception e) {

        } finally {
            closeResource(jedis);
        }
        return null;
    }

    /**
     *   向缓存集合中追加元素 如果key存在，则追加，否则新增
     * @param key
     * @param object
     * @param <T>
     * @return 追加成功返回true，否则false
     */
    public <T> boolean appendObjectToList(String key,Object object){
        if (stringUtil.isNullOrEmpty(key) || object == null){
            return false;
        }
        List<Object> list = getList(key);
        if (list == null){
            list = new ArrayList<>();
        }
        //先清除原先的缓存
        getJedis().del(key.getBytes());
        list.add(object);
        cacheList(key,list);
        return true;
    }

    /**
     * 从缓存中删除question
     * @param key
     * @param questionId
     */
    public void deleteQuestionsFromList(String key,Long questionId){
        List<Question> questions = getList(key);
        if (questions != null){
            for (Question q : questions){
                if (q.getQuestionId() == questionId){
                    questions.remove(q);
                    break;
                }
            }
            getJedis().del(key);
            cacheList(key,questions);
        }
    }

    /**
     *
     * @param key
     * @param question
     */
    public void updateQuestionsToList(String key,Question question){
        List<Question> questions = getList(key);
        if (questions == null){
            appendObjectToList(key,question);
        }
        else {
            for(Question q : questions){
                if (q.getQuestionId() == question.getQuestionId()){
                    questions.remove(q);
                    break;
                }
            }
            //关闭资源，先这样写
            getJedis().del(key.getBytes());
            questions.add(question);
            cacheList(key,questions);
        }
    }

    private <T> boolean updateObjectToList(String key,Object object){
        if (stringUtil.isNullOrEmpty(key) || object == null){
            return false;
        }
        List<Object> list = getList(key);
        if (list == null){
            list = new ArrayList<>();
        }
        list.add(object);
        cacheList(key,list);
        return true;
    }

    /**
     *  Google protostuff序列化 可以是对象，也可以是集合
     * @param obj
     * @return
     */
    private byte[] serialize(Object obj){
        if (obj == null){
            //null不能为序列化
            throw new NullPointerException("value cannot be serialized");
        }
        //抛NullPointerException
        RuntimeSchema<Object> schema = RuntimeSchema.createFrom(Object.class);
        byte[] bytes = ProtostuffIOUtil.toByteArray(obj, schema,
                LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        return bytes;
    }

    /**
     * Google protostuff反序列化
     * @param bytes
     * @return
     */
    private Object deserialize(byte[] bytes){
        Object obj = null;
        if (bytes != null){
            RuntimeSchema<Object> schema = RuntimeSchema.createFrom(Object.class);
            //构造一个空的反序列化对象
            obj = schema.newMessage();
            ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        }
        return obj;
    }

    /**
     * java方式序列化  对象必须实现Serializable接口
     * @param value
     * @return
     */
    public byte[] serialize1(Object value) {
        if (value == null) {
            throw new NullPointerException("Can't serialize null");
        }
        byte[] rv=null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;
        try {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            os.writeObject(value);
            os.close();
            bos.close();
            rv = bos.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("Non-serializable object", e);
        } finally {
            try {
                if(os!=null)os.close();
                if(bos!=null)bos.close();
            }catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return rv;
    }

    /**
     *  java方式反序列化
     * @param in
     * @return
     */
    public Object deserialize1(byte[] in) {
        Object rv=null;
        ByteArrayInputStream bis = null;
        ObjectInputStream is = null;
        try {
            if(in != null) {
                bis=new ByteArrayInputStream(in);
                is=new ObjectInputStream(bis);
                rv=is.readObject();
                is.close();
                bis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(is!=null)is.close();
                if(bis!=null)bis.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return rv;
    }

}
