import groovy.transform.EqualsAndHashCode

import java.util.regex.Matcher

class Parser {

    def static parse(String formula){
        def tokens = []
        Token t
        String res
        while (((t, res) = getToken(formula)))

        return res
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
        }
        return new Tuple2<Token, String>(token, res)
    }

    def static sumUpElements(Element[] elements) {
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

}
