/*
 * @Author: your name
 * @Date: 2020-05-15 16:49:04
 * @LastEditTime: 2020-05-15 16:56:29
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \PLO\src\main\java\Cores\GrammarError.java
 */ 
package Exceptions;

public class GrammarError extends Throwable{
    public GrammarError(String message){
        super(message);
    }
}
