/*
 * @Author: your name
 * @Date: 2020-05-07 16:18:40
 * @LastEditTime: 2020-05-10 15:14:10
 * @LastEditors: Haotian Bai
 * @Description: In User Settings Edit
 * @FilePath: \PLO\src\main\java\Utils\Token\Terminal.java
 */
package Utils.Token;

import java.util.Objects;
/**
 * Terminal is the parent class for 
 */
public class Terminal extends Token {
    public Terminal(final String type,String letter) {
        super(type,letter);
    }
    @Override public boolean equals(Object o) {
        if (this == o){
            return true;
        } 
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        NonTerminal nonTerminal = (NonTerminal) o;
        return this.context.equals(nonTerminal.context) && this.type == nonTerminal.type;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.context,this.type);
    }
    
}