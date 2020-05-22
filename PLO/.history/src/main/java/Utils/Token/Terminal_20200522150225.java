/*
 * @Author: your name
 * @Date: 2020-05-07 16:18:40
 * @LastEditTime: 2020-05-22 15:02:25
 * @LastEditors: Haotian Bai
 * @Description: In User Settings Edit
 * @FilePath: \PLO\src\main\java\Utils\Token\Terminal.java
 */
package Utils.Token;

import java.util.Objects;
/**
 * Terminal is the parent class for subclasses like OPE,NUM,WORD,
 * and its type is varied. Be careful not to use it as the sign to compare with NonTerminal.
 */
public class Terminal extends Token {
    public String context;
    public Terminal(final String type,String name,String context) {
        super(type,name);
        this.context = context;
    }
    public Terminal(final String type,String name) {
        super(type,name);
    }
    @Override public boolean equals(Object o) {
        if (this == o){
            return true;
        } 
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        NonTerminal nonTerminal = (NonTerminal) o;
        return this.name.equals(nonTerminal.name) &&&& this.type == nonTerminal.type;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.name,this.type);
    }
    
}
