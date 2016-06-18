package net.revenj.patterns

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

/** Olap cube is online analytical processing concept used for extracting business intelligence.
  * At it's core it's just a grouping of data by some dimensions and aggregation
  * of values through facts. Facts can be sum, count, distinct and various others concepts.
  * Cube can be made from various data sources: aggregates, snowflakes, SQL, LINQ, etc...
  *
  * DSL example:
  * <pre>
  * module Finance {
  * aggregate Payment {
  * DateTime createdAt { versioning; }
  * String   account;
  * Money    total;
  * calculated Int year from 'it => it.Year';
  * }
  *
  * cube&lt;Payment&gt; Analysis {
  * dimension account;
  * dimension year;
  * count     createdAt;
  * sum       total;
  * }
  * }
  * </pre>
  */
trait OlapCubeQuery[T <: DataSource] {
  val dimensions: Set[String]
  val facts: Set[String]

  def analyze(dimensions: Seq[String],
              facts: Seq[String],
              order: Seq[(String, Boolean)] = Seq.empty,
              filter: Option[Specification[T]] = None,
              limit: Option[Int] = None,
              offset: Option[Int] = None): Future[IndexedSeq[Map[String, Any]]]
}

object OlapCubeQuery {

  //TODO: pimp for builder

  private class OlapCubeQueryBuilder[T <: DataSource](query: OlapCubeQuery[T]) {
    private val dimensions = ArrayBuffer.newBuilder[String]
    private val facts = ArrayBuffer.newBuilder[String]
    private var resultLimit: Option[Int] = None
    private var resultOffset: Option[Int] = None
    private val order = ArrayBuffer.newBuilder[(String, Boolean)]

    def use(dimensionOrFact: String): OlapCubeQueryBuilder[T] = {
      require(dimensionOrFact ne null, "null value provided for dimension or fact")
      require(dimensionOrFact.length != 0, "empty value provided for dimension or fact")

      if (query.dimensions.contains(dimensionOrFact)) {
        dimensions += dimensionOrFact
      } else if (query.facts.contains(dimensionOrFact)) {
        facts += dimensionOrFact
      } else {
        throw new IllegalArgumentException("Unknown dimension or fact: " + dimensionOrFact + ". Use dimensions or facts method for available dimensions and facts")
      }
      this
    }

    def ascending(result: String): OlapCubeQueryBuilder[T] = orderBy(result, ascending = true)

    def descending(result: String): OlapCubeQueryBuilder[T] = orderBy(result, ascending = false)

    private def orderBy(result: String, ascending: Boolean) = {
      require(query.dimensions.contains(result) || query.facts.contains(result), "Unknown result: " + result + ". Result can be only field from used dimensions and facts.")
      order += result -> ascending
      this
    }

    def take(count: Int): OlapCubeQueryBuilder[T] = limit(count)

    def limit(count: Int): OlapCubeQueryBuilder[T] = {
      require(count > 0, "Invalid limit value. Limit must be positive")
      resultLimit = Some(count)
      this
    }

    def drop(count: Int): OlapCubeQueryBuilder[T] = offset(count)

    def offset(count: Int): OlapCubeQueryBuilder[T] = {
      require(count > 0, "Invalid offset value. Offset must be positive")
      resultOffset = Some(count)
      this
    }

    def analyze(specification: Option[Specification[T]] = None): Future[IndexedSeq[Map[String, Any]]] = {
      query.analyze(dimensions.result(), facts.result(), order.result(), specification, resultLimit, resultOffset)
    }
  }
}
