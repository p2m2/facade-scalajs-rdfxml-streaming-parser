package com.github.p2m2.facade

import io.scalajs.nodejs.fs.Fs
import utest._

import scala.language.implicitConversions
import scala.scalajs.js.JSON

object N3ParserTest extends TestSuite {
  val tests = Tests {

    test("Manually write strings to the parser") {

      val myParser = new RdfXmlParser()

      myParser
        .on("data", (data : Quad) => {
          println(" -- data --")
          println(JSON.stringify(data.graph))
          println(JSON.stringify(data))})
        .on("error", (error : String) => {println(error)})
        .on("end", () => { println("All triples were parsed!")})

      myParser.write("""<?xml version="1.0"?>""")
      myParser.write("""<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                  xmlns:ex="http://example.org/stuff/1.0/"
                  xml:base="http://example.org/triples/">""")
      myParser.write("""<rdf:Description rdf:about="http://www.w3.org/TR/rdf-syntax-grammar">""")
      myParser.write("""<ex:prop />""")
      myParser.write("""</rdf:Description>""")
      myParser.write("""</rdf:RDF>""")
      myParser.end()


    }

    test("Import streams") {
      val myParser = new RdfXmlParser();
      val myTextStream = Fs.createReadStream("src/test/resources/animals.rdf")

      myParser.`import`(myTextStream)
        .on("data", (data : Quad) => {
          println(" -- data --")
          println(JSON.stringify(data.graph))
          println(JSON.stringify(data))})
        .on("error", (error : String) => {println(error)})
        .on("end", () => { println("All triples were parsed!")})
    }
  }
}
