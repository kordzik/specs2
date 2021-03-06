package org.specs2
package reporter

import mock._
import specification.{Tags, Fragments}
import main.Arguments
import matcher.DataTables
import org.scalatools.testing.{Event, Logger, EventHandler}

class TestInterfaceReporterSpec extends Specification with DataTables with Tags { def is = section("cloudbees")^
                                                                                                                        p^
  "A TestInterfaceReporter should"                                                                                      ^
    "print the specification title if defined"                                                                          ! report().e1^
    "report datatables ok"                                                                                              ! report().e2^
                                                                                                                        end

  case class report() extends Mockito {
    val logger = mock[Logger]
    val handler = mock[EventHandler]

    val reporter = new TestInterfaceReporter(handler, Array(logger))
    def report(fs: =>Fragments, a: Arguments = args(color=false)) = reporter.report(new Specification { def is = fs})(a)

    def e1 = {
      report("title".title ^ "text")
      there was one(logger).info("title")
    }
    def e2 = {
      report("ex" ! ("a" | "b" |> 1 ! 2 | { (a, b) => success } ))
      there was atLeastOne(handler).handle(any[Event])
    }
  }
}