package calculator.parser

import org.scalatest._

import calculator.ir._
import calculator.parser._
import edu.hmc.langtools._

class CalcParserTests extends FunSpec with LangParseMatchers[AST] {

  override val parser = CalcParser.apply _
  
  describe("A number") {

    it("can be a single digit") {
      program("1") should parseAs ( 1 )
    }
    
    it ("can be multiple digits") {
      program("10") should parseAs ( 10 )
      program("121") should parseAs ( 121 )
    }
    
    it ("can be a negative number") {
      program("-10") should parseAs ( -10 )
    }
    
    it ("cannot be floating-point number") {
      program("1.1") should not (parse)
      program(" .3") should not (parse)
    }

  }
  
  describe("Addition") {

    it("can add two numbers") {
      program("1+1") should parseAs ( 1 |+| 1 )
    }
    
    it("can be chained (and is left-associative)") {
      program("1 + 2 + 100") should parseAs ( (1 |+| 2) |+| 100 )
    }

  }
  
  describe("Subtraction") {
    it("can subtract two numbers") {
      program("1-1") should parseAs ( 1 |-| 1 )
    }
    
    it("can be chained (and is left-associative)") {
      program("1 - 2 - 100") should parseAs ( (1 |-| 2) |-| 100 )
      program("1 - 2 + 100") should parseAs ( (1 |-| 2) |+| 100 )
    }
  }
  
  describe("Multiplication") {
    it("can multiply two numbers") {
      program("1*1") should parseAs ( 1 |*| 1 )
    }
    
    it("can be chained (and is left-associative)") {
      program("1 * 2 + 100") should parseAs ( (1 |*| 2) |+| 100 )
      program("1 + 2 * 100") should parseAs ( 1 |+| (2 |*| 100) )
    }
  }
  
  describe("Division") {
    it("can divide two numbers") {
      program("1/1") should parseAs ( 1 |/| 1 )
    }
    
    it("can be chained (and is left-associative)") {
      program("1 / 2 / 100") should parseAs ( (1 |/| 2) |/| 100 )
    }
  }
  
  describe("Parentheses") {
    it("should not change a value") {
      program("(4)") should parseAs (4)
    }
    it("can order things") {
      program("3+(1-2)") should parseAs ( 3 |+| (1 |-| 2))
    }
  }
}
