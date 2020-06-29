/*
 * @Author: your name
 * @Date: 2020-05-07 16:18:40
 * @LastEditTime: 2020-05-22 15:32:06
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
    public Terminal(final String type,String subType,String context) {
        super(type,subType,context);
    }
    public Terminal(String context) {
        super("Terminal","",context);
    }
    @Override public boolean equals(Object o) {
        if (this == o){
            return true;
        } 
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Terminal terminal = (Terminal) o;
        return this.context.equals(terminal.context);
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.context);
    }
    
}
