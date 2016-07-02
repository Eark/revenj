package example.test.postgres


import net.revenj.patterns._
import net.revenj.database.postgres._
import net.revenj.database.postgres.converters._

class AbcWriteConverter(allColumns: List[net.revenj.database.postgres.ColumnInfo], container: net.revenj.extensibility.Container) extends Converter[example.test.AbcWrite] {

	private val columns = allColumns.filter(it => it.typeSchema == "test" && it.typeName == "AbcWrite")
	private val extendedColumns = allColumns.filter(it => it.typeSchema == "test" && it.typeName == "AbcWrite")

	val dbName = """test.Abc"""
	def default() = null

	private val columnCount = columns.size
	private val readers = new Array[(example.test.AbcWrite, PostgresReader, Int) => example.test.AbcWrite](if (columnCount == 0) 1 else columnCount)
	private val extendedColumnCount = extendedColumns.size
	private val extendedReaders = new Array[(example.test.AbcWrite, PostgresReader, Int) => example.test.AbcWrite](if (extendedColumnCount == 0) 1 else extendedColumnCount)

	for (i <- readers.indices) {
		readers(i) = (it, rdr, ctx) => { StringConverter.skip(rdr, ctx); it }
	}
	for (i <- extendedReaders.indices) {
		extendedReaders(i) = (it, rdr, ctx) => { StringConverter.skip(rdr, ctx); it }
	}

	container.registerInstance(this, handleClose = false)
	container.registerInstance[Converter[example.test.AbcWrite]](this, handleClose = false)

	override def parseCollectionItem(reader: PostgresReader, context: Int): example.test.AbcWrite = {
		val cur = reader.read()
		if (cur == 'N') {
		  reader.read(4)
		  null
		} else {
		  from(reader, 0, context)
		}		
	}
	override def parseNullableCollectionItem(reader: PostgresReader, context: Int): Option[example.test.AbcWrite] = {
		val cur = reader.read()
		if (cur == 'N') {
		  reader.read(4)
		  None
		} else {
		  Some(from(reader, 0, context))
		}
	}

	override def toTuple(item: example.test.AbcWrite): PostgresTuple = {
		val items = new Array[PostgresTuple](columnCount) 
		items(IDPos) = net.revenj.database.postgres.converters.IntConverter.toTuple(item.ID)
		items(sPos) = net.revenj.database.postgres.converters.StringConverter.toTuple(item.s)
		items(iiPos) = net.revenj.database.postgres.converters.ArrayTuple.createIndexed(item.ii, net.revenj.database.postgres.converters.IntConverter.toTuple)
		items(enPos) = converterexample_test_En.toTuple(item.en)
		items(en2Pos) = if (item.en2.isEmpty) net.revenj.database.postgres.converters.PostgresTuple.NULL else converterexample_test_En.toTuple(item.en2.get)
		items(en3Pos) = net.revenj.database.postgres.converters.ArrayTuple.createSeq(item.en3, example.test.postgres.EnConverter.toTuple)
		items(i4Pos) = net.revenj.database.postgres.converters.ArrayTuple.createSeq(item.i4, net.revenj.database.postgres.converters.IntConverter.toTuple)
		items(anotherPos) = net.revenj.database.postgres.converters.ArrayTuple.createSeq(item.another, converterexample_test_Another.toTuple)
		items(vPos) = converterexample_test_Val.toTuple(item.v)
		items(vvPos) = if (item.vv.isEmpty) net.revenj.database.postgres.converters.PostgresTuple.NULL else converterexample_test_Val.toTuple(item.vv.get)
		items(iiiPos) = if (item.iii.isEmpty) net.revenj.database.postgres.converters.PostgresTuple.NULL else net.revenj.database.postgres.converters.ArrayTuple.createIndexed(item.iii.get, net.revenj.database.postgres.converters.IntConverter.toTuple)
		items(iiiiPos) = net.revenj.database.postgres.converters.ArrayTuple.createIndexedOption(item.iiii, net.revenj.database.postgres.converters.IntConverter.toTuple)
		items(ssPos) = net.revenj.database.postgres.converters.StringConverter.toTuple(item.ss)
		items(vvvPos) = net.revenj.database.postgres.converters.ArrayTuple.createIndexed(item.vvv, converterexample_test_Val.toTuple)
		items(aPos) = net.revenj.database.postgres.converters.ArrayTuple.createSetOption(item.a, converterexample_test_Another.toTuple)
		items(sssPos) = net.revenj.database.postgres.converters.ArrayTuple.createSeq(item.sss, net.revenj.database.postgres.converters.StringConverter.toTuple)
		items(ssssPos) = if (item.ssss.isEmpty) net.revenj.database.postgres.converters.PostgresTuple.NULL else net.revenj.database.postgres.converters.ArrayTuple.createSeqOption(item.ssss.get, net.revenj.database.postgres.converters.StringConverter.toTuple)
		RecordTuple(items)
	}

	def toTupleExtended(item: example.test.AbcWrite): PostgresTuple = {
		val items = new Array[PostgresTuple](extendedColumnCount) 
		items(IDPosExtended) = net.revenj.database.postgres.converters.IntConverter.toTuple(item.ID)
		items(sPosExtended) = net.revenj.database.postgres.converters.StringConverter.toTuple(item.s)
		items(iiPosExtended) = net.revenj.database.postgres.converters.ArrayTuple.createIndexed(item.ii, net.revenj.database.postgres.converters.IntConverter.toTuple)
		items(enPosExtended) = converterexample_test_En.toTuple(item.en)
		items(en2PosExtended) = if (item.en2.isEmpty) PostgresTuple.NULL else converterexample_test_En.toTuple(item.en2.get)
		items(en3PosExtended) = net.revenj.database.postgres.converters.ArrayTuple.createSeq(item.en3, example.test.postgres.EnConverter.toTuple)
		items(i4PosExtended) = net.revenj.database.postgres.converters.ArrayTuple.createSeq(item.i4, net.revenj.database.postgres.converters.IntConverter.toTuple)
		items(anotherPosExtended) = net.revenj.database.postgres.converters.ArrayTuple.createSeq(item.another, converterexample_test_Another.toTupleExtended)
		items(vPosExtended) = converterexample_test_Val.toTupleExtended(item.v)
		items(vvPosExtended) = if (item.vv.isEmpty) PostgresTuple.NULL else converterexample_test_Val.toTupleExtended(item.vv.get)
		items(iiiPosExtended) = if (item.iii.isEmpty) net.revenj.database.postgres.converters.PostgresTuple.NULL else net.revenj.database.postgres.converters.ArrayTuple.createIndexed(item.iii.get, net.revenj.database.postgres.converters.IntConverter.toTuple)
		items(iiiiPosExtended) = net.revenj.database.postgres.converters.ArrayTuple.createIndexedOption(item.iiii, net.revenj.database.postgres.converters.IntConverter.toTuple)
		items(ssPosExtended) = net.revenj.database.postgres.converters.StringConverter.toTuple(item.ss)
		items(vvvPosExtended) = net.revenj.database.postgres.converters.ArrayTuple.createIndexed(item.vvv, converterexample_test_Val.toTupleExtended)
		items(aPosExtended) = net.revenj.database.postgres.converters.ArrayTuple.createSetOption(item.a, converterexample_test_Another.toTupleExtended)
		items(sssPosExtended) = net.revenj.database.postgres.converters.ArrayTuple.createSeq(item.sss, net.revenj.database.postgres.converters.StringConverter.toTuple)
		items(ssssPosExtended) = if (item.ssss.isEmpty) net.revenj.database.postgres.converters.PostgresTuple.NULL else net.revenj.database.postgres.converters.ArrayTuple.createSeqOption(item.ssss.get, net.revenj.database.postgres.converters.StringConverter.toTuple)
		RecordTuple(items)
	}

	def parseExtended(reader: PostgresReader, context: Int): example.test.AbcWrite = {
		val cur = reader.read()
		if (cur == ',' || cur == ')') {
			default()
		} else {
			val result = fromExtended(reader, context, if (context == 0) 1 else context << 1)
			reader.read()
			result
		}
	}

	def parseOptionExtended(reader: PostgresReader, context: Int): Option[example.test.AbcWrite] = {
		val cur = reader.read()
		if (cur == ',' || cur == ')') {
			None
		} else {
			val result = Some(fromExtended(reader, context, if (context == 0) 1 else context << 1))
			reader.read()
			result
		}
	}

	
	
	container.registerClass[example.test.postgres.AbcWriteRepository](classOf[example.test.postgres.AbcWriteRepository], singleton = false)
	container.registerFactory[net.revenj.patterns.SearchableRepository[example.test.AbcWrite]](c => new example.test.postgres.AbcWriteRepository(c), singleton = false)
	container.registerFactory[net.revenj.patterns.PersistableRepository[example.test.AbcWrite]](c => new example.test.postgres.AbcWriteRepository(c), singleton = false)
	
	def buildURI(_sw: net.revenj.database.postgres.PostgresBuffer, instance: example.test.AbcWrite): String = {
		buildURI(instance.ID, _sw)
	}
	def buildURI(ID: Int, _sw: net.revenj.database.postgres.PostgresBuffer): String = {
		_sw.initBuffer()
		val _tmp: String = null
		
		net.revenj.database.postgres.converters.IntConverter.serializeURI(_sw, ID)
		_sw.bufferToString()
	}
	private val IDPos = columns.find(it => it.columnName == "ID") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "ID" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val IDPosExtended = extendedColumns.find(it => it.columnName == "ID") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "ID" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val sPos = columns.find(it => it.columnName == "s") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "s" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val sPosExtended = extendedColumns.find(it => it.columnName == "s") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "s" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val iiPos = columns.find(it => it.columnName == "ii") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "ii" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val iiPosExtended = extendedColumns.find(it => it.columnName == "ii") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "ii" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val enPos = columns.find(it => it.columnName == "en") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "en" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	lazy val converterexample_test_En = example.test.postgres.EnConverter
	private val enPosExtended = extendedColumns.find(it => it.columnName == "en") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "en" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val en2Pos = columns.find(it => it.columnName == "en2") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "en2" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val en2PosExtended = extendedColumns.find(it => it.columnName == "en2") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "en2" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val en3Pos = columns.find(it => it.columnName == "en3") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "en3" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val en3PosExtended = extendedColumns.find(it => it.columnName == "en3") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "en3" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val i4Pos = columns.find(it => it.columnName == "i4") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "i4" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val i4PosExtended = extendedColumns.find(it => it.columnName == "i4") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "i4" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	lazy val converterexample_test_Another = container.resolve[example.test.postgres.AnotherConverter]
	private val anotherPos = columns.find(it => it.columnName == "another") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "another" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val anotherPosExtended = extendedColumns.find(it => it.columnName == "another") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "another" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	lazy val converterexample_test_Val = container.resolve[example.test.postgres.ValConverter]
	private val vPos = columns.find(it => it.columnName == "v") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "v" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val vPosExtended = extendedColumns.find(it => it.columnName == "v") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "v" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val vvPos = columns.find(it => it.columnName == "vv") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "vv" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val vvPosExtended = extendedColumns.find(it => it.columnName == "vv") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "vv" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val iiiPos = columns.find(it => it.columnName == "iii") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "iii" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val iiiPosExtended = extendedColumns.find(it => it.columnName == "iii") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "iii" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val iiiiPos = columns.find(it => it.columnName == "iiii") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "iiii" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val iiiiPosExtended = extendedColumns.find(it => it.columnName == "iiii") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "iiii" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val ssPos = columns.find(it => it.columnName == "ss") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "ss" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val ssPosExtended = extendedColumns.find(it => it.columnName == "ss") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "ss" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val vvvPos = columns.find(it => it.columnName == "vvv") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "vvv" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val vvvPosExtended = extendedColumns.find(it => it.columnName == "vvv") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "vvv" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val aPos = columns.find(it => it.columnName == "a") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "a" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val aPosExtended = extendedColumns.find(it => it.columnName == "a") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "a" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val sssPos = columns.find(it => it.columnName == "sss") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "sss" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val sssPosExtended = extendedColumns.find(it => it.columnName == "sss") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "sss" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val ssssPos = columns.find(it => it.columnName == "ssss") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "ssss" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		
	private val ssssPosExtended = extendedColumns.find(it => it.columnName == "ssss") match {
		case Some(col) => col.order - 1
		case None => throw new IllegalArgumentException("""Couldn't find column "ssss" in type test.AbcWrite. Check if database is out of sync with code!""")
	}		

	
	override def parseRaw(reader: PostgresReader, start: Int, context: Int): example.test.AbcWrite = {
		val result = from(reader, context, if (context == 0) 1 else context << 1)
		reader.read()
		result
	}

	def from(reader: PostgresReader, outerContext: Int, context: Int): example.test.AbcWrite = {
		reader.read(outerContext)
		val result = example.test.AbcWrite.buildInternal(reader, context, this, readers)
		reader.read(outerContext)
		result
	}

	def parseRawExtended(reader: PostgresReader, start: Int, context: Int): example.test.AbcWrite = {
		val result = fromExtended(reader, context, if (context == 0) 1 else context << 1)
		reader.read()
		result
	}

	def fromExtended(reader: PostgresReader, outerContext: Int, context: Int): example.test.AbcWrite = {
		reader.read(outerContext)
		val result = example.test.AbcWrite.buildInternal(reader, context, this, extendedReaders)
		reader.read(outerContext)
		result
	}

	def initialize():Unit = {
		
	
		example.test.AbcWrite.configureConverters(readers, IDPos, sPos, iiPos, enPos, en2Pos, en3Pos, i4Pos, anotherPos, converterexample_test_Another, vPos, converterexample_test_Val, vvPos, converterexample_test_Val, iiiPos, iiiiPos, ssPos, vvvPos, converterexample_test_Val, aPos, converterexample_test_Another, sssPos, ssssPos)
		example.test.AbcWrite.configureExtendedConverters(extendedReaders, IDPosExtended, sPosExtended, iiPosExtended, enPosExtended, en2PosExtended, en3PosExtended, i4PosExtended, anotherPosExtended, converterexample_test_Another, vPosExtended, converterexample_test_Val, vvPosExtended, converterexample_test_Val, iiiPosExtended, iiiiPosExtended, ssPosExtended, vvvPosExtended, converterexample_test_Val, aPosExtended, converterexample_test_Another, sssPosExtended, ssssPosExtended)
	}
}
