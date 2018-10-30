import com.sun.istack.internal.FragmentContentHandler
import groovy.transform.EqualsAndHashCode

import java.util.regex.Matcher

class Parser {

    static final LEFT_PARENTHESIS = ['(','[','{']
    static final RIGHT_PARENTHESIS = [')',']','}']

    def static parse(String formula){
        def tokens = []
        Token t
        String res
        for(res = formula; res.length() > 0 ; ){
            (t,res) = getToken(res)
            tokens << t
        }
        return sumUpElements(tokens)
    }


    interface Token {}

    static Tuple2<Token,String> getToken(String formula) {
        Token token
        String res = formula
        def elementMatcher = ~$/^(\p{javaUpperCase}\p{javaLowerCase}?)(\p{Digit}?)/$

        Matcher match
        if((match = elementMatcher.matcher(formula)) || match.matches()){
            def multiplyer = match[0][2]
            token = new Element(match[0][1],multiplyer?Integer.parseInt(multiplyer):1)
            res = formula - match[0][0]
        } else if(formula[0] in LEFT_PARENTHESIS) {
            scanForOther(formula)
        }
        return new Tuple2<Token, String>(token, res)
    }

    static Fragment scanForFragment(String formula) {
        StringBuffer res = new StringBuffer("")
        Integer multiplayer = 1
        Integer parenthesisDepth = 0

        for (int i = 0; i < formula.length(); i++) {
            String c = formula[i]
            if(c in LEFT_PARENTHESIS) {
                parenthesisDepth++
                continue
            } else if(c in RIGHT_PARENTHESIS){
                parenthesisDepth--
                if(parenthesisDepth==0){
                    if(i < formula.length()-1){
                        String n = formula[i+1]
                        if(n.isNumber()){
                            multiplayer = Integer.parseInt(n)
                        }
                    }
                    break
                }
                continue
            }
            res << formula[i]
        }

        return new Fragment(res.toString(),multiplayer)
    }

    def static sumUpElements(List<Element> elements) {
        def res = [:]
        for (Element element : elements) {
            if(null == res[element.name]) {
                res[element.name] = element.number
            }else {
                res[element.name] += element.number
            }
        }
        return res
    }


    @EqualsAndHashCode
    static class Element implements Token {
        String name
        Integer number = 1

        Element(String name, Integer number) {
            this.name = name
            this.number = number==0?1:number
        }

        Element() {
        }
    }

    @EqualsAndHashCode
    static class Fragment implements Token {
        String fragment
        Integer multiplayer

        Fragment(String fragment, Integer multiplayer) {
            this.fragment = fragment
            this.multiplayer = multiplayer==0?1:multiplayer
        }

        Fragment() {
        }
    }

}
