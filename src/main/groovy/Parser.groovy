class Parser {

    def static parse(String formula){
        def res = [:]

        return res
    }

    class Element implements Token {
        String name
        Integer number = 1
    }

    interface Token {}

    Token getToken(String formula) {
        return null
    }

    def sumUpTokens(Element[] elements) {
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
}
