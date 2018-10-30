import spock.lang.Specification
import spock.lang.Unroll

import javax.xml.bind.Element

import static Parser.sumUpElements


class ParserTest extends Specification {

    @Unroll
    def "test parse #formula"() {
        expect:
        res == Parser.parse(formula)
        where:
        formula || res
        ""      || [:]
        "O"     || ["O": 1]
        "O2"    || ["O": 2]
        "H2O"   || ["O": 1, "H": 2]
    }

    def "test summing up elements"() {
        given:
        List<Parser.Element> elements = [["O", 1], ["O", 1], ["Mg", 1]]
        when:
        def sum = sumUpElements(elements)
        then:
        sum == ["O": 2, "Mg": 1]
    }

    @Unroll
    def "test simply cases of getToken"() {
        expect:
        new Tuple2<Parser.Element, String>(element, residue) == Parser.getToken(formula)
        where:
        formula      || element                     | residue
        ""           || null                        | ""
        "Mg2O3"      || new Parser.Element("Mg", 2) | "O3"
        "Mg"         || new Parser.Element("Mg", 1) | ""
        "Mg(OH)2"    || new Parser.Element("Mg", 1) | "(OH)2"
        "(Mg(OH)2)2" || null                        | "(Mg(OH)2)2"
        "o3"         || null                        | "o3"
        "MgO"        || new Parser.Element("Mg", 1) | "O"
        "O"          || new Parser.Element("O", 1)  | ""
        "O2"         || new Parser.Element("O", 2)  | ""
        "OMg"        || new Parser.Element("O", 1)  | "Mg"
    }

    @Unroll
    def "test scanning parenthesis for fragments"() {
        expect:
        new Parser.Fragment(fragment, multiplayer) == Parser.scanForFragment(formula)
        where:
        formula || fragment | multiplayer
        ""      || ""       | 1
        "()"    || ""       | 1
        "(())"  || ""       | 1
        "(O)"   || "O"      | 1
        "(O)2"  || "O"      | 2
        "(OH)2" || "OH"     | 2
    }

}
