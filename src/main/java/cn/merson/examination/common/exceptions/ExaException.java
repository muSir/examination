package cn.merson.examination.common.exceptions;

/**
 * @Description:
 * @Author: created by Merson
 * Created on 2017/12/12 0012 15:08
 */
public class ExaException extends RuntimeException {
        private String message;

        public ExaException(String errorMessage){
            super(errorMessage);
            this.message = errorMessage;
        }

}
