package org.revenj

import java.io.IOException
import java.util.Optional

import gen.model.Boot
import gen.model.adt.{Anonymous, User}
import org.revenj.extensibility.Container
import org.junit._
import org.revenj.patterns._
import org.revenj.database.postgres.jinq.{JinqMetaModel, ScalaSort, ScalaSpecification}
import ru.yandex.qatools.embed.service.PostgresEmbeddedService

import scala.util.Random

class TestLambda {

  private var postgres: PostgresEmbeddedService = null
  private var container: Container = null

  @Before
  @throws[IOException]
  def initContainer {
    postgres = new PostgresEmbeddedService("localhost", 5555, "revenj", "revenj", "revenj", "target/db", true, 5000)
    postgres.start
    container = Boot.configure("jdbc:postgresql://localhost:5555/revenj?user=revenj&password=revenj").asInstanceOf[Container]
  }

  @After
  @throws[Exception]
  def closeContainer {
    container.close
    postgres.stop
  }

  class RichQuery[T <: DataSource](query: Query[T]) {
    def where(predicate: T => Boolean) = query.filter(new ScalaSpecification[T](predicate))
  }

  implicit def where[T <: DataSource](query: Query[T]): RichQuery[T] = new RichQuery[T](query)

  @Test
  def testSimpleLambda {
    val ctx = container.resolve(classOf[DataContext])
    val name = "random_user"
    val found = ctx.find(classOf[User], name)
    if (found.isPresent) {
      ctx.delete(found.get)
    }
    val user = new User().setUsername(name).setAuthentication(new Anonymous)
    ctx.create(user)
    val result = ctx.query(classOf[User]).where(it => it.getUsername == "random_user").findAny()
    Assert.assertTrue(result.isPresent)
    Assert.assertTrue(result.get().deepEquals(user))
  }

  //TODO: variable arguments are not working ;(
  //@Test
  def testSimpleLambdaWithArg {
    val ctx = container.resolve(classOf[DataContext])
    val name = "random_arg_user"
    val found = ctx.find(classOf[User], name)
    if (found.isPresent) {
      ctx.delete(found.get)
    }
    val user = new User().setUsername(name).setAuthentication(new Anonymous)
    ctx.create(user)
    val arg = "random_arg_user"
    val result = ctx.query(classOf[User]).where(it => it.getUsername == arg).findAny()
    Assert.assertTrue(result.isPresent)
    Assert.assertTrue(result.get().deepEquals(user))
  }

  class RichSort[T <: DataSource, V <: java.lang.Comparable[V]](query: Query[T]) {
    def orderBy(fn: T => V)= query.sortedBy(new ScalaSort[T, V](fn))
  }

  implicit def orderBy[T <: DataSource, V <: java.lang.Comparable[V]](query: Query[T]): RichSort[T, V] = new RichSort[T, V](query)

  @Test
  def testSimpleOrder {
    val ctx = container.resolve(classOf[DataContext])
    val result = ctx.query(classOf[User]).orderBy(it => it.getUsername).findAny()
    Assert.assertNotNull(result)
  }

  @Test
  def testOrderFromMethod {
    val ctx = container.resolve(classOf[DataContext])
    var mm = container.resolve(classOf[JinqMetaModel])
    val getter = mm.findGetter(classOf[User].getMethod("getUsername")).asInstanceOf[Query.Compare[User, _]]
    val result = ctx.query(classOf[User]).sortedDescendingBy(getter).findAny()
    Assert.assertNotNull(result)
  }
}