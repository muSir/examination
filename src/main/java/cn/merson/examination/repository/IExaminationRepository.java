package cn.merson.examination.repository;

import cn.merson.examination.entity.Examination;
import org.apache.catalina.LifecycleState;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @Description: 考试记录
 * @Author: created by Merson
 * Created on 2017/12/4 0004 22:47
 */
public interface IExaminationRepository extends CrudRepository<Examination,Long> {

    /**
     * 获取指定考生所有的考试记录
     * @param userId
     * @return
     */
    List<Examination> getAllByUserId(Long userId);
}
