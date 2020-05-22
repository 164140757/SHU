/*
 * @Author: your name
 * @Date: 2020-05-15 16:49:04
 * @LastEditTime: 2020-05-21 14:48:55
 * @LastEditors: Haotian Bai
 * @Description: In User Settings Edit
 * @FilePath: \PLO\src\main\java\Exceptions\GrammarError.java
 */ 
package Exceptions;

public class GrammarError extends Throwable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public GrammarError(String message) {
        super(message);
    }
}
