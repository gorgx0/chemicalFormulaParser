import spock.lang.Specification
import spock.lang.Unroll


class ParserTest extends Specification {

    @Unroll
    def "test parse #formula"() {
        expect:
        res == Parser.parse(formula)
        where:
        formula || res
        ""      || [:]
        "O"     || ["O": 1]
        "O2"     || ["O": 2]
        "H2O"     || ["O": 2, "H":1]
    }
}
