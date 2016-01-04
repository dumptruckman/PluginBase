package pluginbase.config;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import org.junit.Before;
import pluginbase.config.datasource.DataSource;
import pluginbase.config.datasource.gson.GsonDataSource;
import pluginbase.config.datasource.hocon.HoconDataSource;
import pluginbase.config.datasource.json.JsonDataSource;
import pluginbase.config.datasource.yaml.YamlDataSource;
import pluginbase.config.examples.Anum;
import pluginbase.config.examples.Child;
import pluginbase.config.examples.Comprehensive;
import pluginbase.config.examples.FakeEnum;
import pluginbase.config.examples.NullContainer;
import pluginbase.config.examples.Parent;
import pluginbase.config.examples.Unknown;
import org.junit.Test;
import pluginbase.config.field.FieldMap;
import pluginbase.config.field.FieldMapper;
import pluginbase.config.serializers.CustomSerializer;
import pluginbase.config.serializers.CustomSerializer2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

public class SerializableConfigTest extends TestBase {

    StringWriter writer;
    int someInt = 125623;
    long someLong = 6012341582323441585L;
    double someDouble = 123565.51231123155D;
    float someFloat = 215415.1215129717F;
    short someShort = 121;
    byte someByte = 12;
    boolean someBoolean = true;
    char someChar = 'h';
    Integer someBoxedInteger = someInt;
    Long someBoxedLong = someLong;
    Double someBoxedDouble = someDouble;
    Float someBoxedFloat = someFloat;
    Short someBoxedShort = someShort;
    Byte someBoxedByte = someByte;
    Boolean someBoxedBoolean = someBoolean;
    Character someBoxedCharacter = someChar;
    BigInteger bigInteger = new BigInteger("54123156663412123956123125691203");
    BigDecimal bigDecimal = new BigDecimal("123451512315674534124975123.123915791712381951957128391479");
    AtomicInteger atomicInteger = new AtomicInteger(someInt);
    AtomicLong atomicLong = new AtomicLong(someLong);
    Anum anum = Anum.C;
    FakeEnum fnum = FakeEnum.FAKE_2;
    UUID uuid = UUID.randomUUID();
    Locale locale = Locale.GERMAN;
    String string = "aharojao r230i 1!@#!=ag04j0gjaka[k2";
    List<Object> list = new ArrayList<>();

    Comprehensive comprehensive = new Comprehensive();

    {
        list.add(someInt);
        list.add(someLong);
        list.add(someDouble);
        list.add(someFloat);
        list.add(someShort);
        list.add(someByte);
        list.add(someBoolean);
        list.add(someChar);
        list.add(bigInteger);
        list.add(bigDecimal);
        list.add(atomicInteger);
        list.add(atomicLong);
        list.add(locale);
        list.add(anum);
        list.add(fnum);
        list.add(uuid);
    }

    Callable<BufferedWriter> sink = new Callable<BufferedWriter>() {
        @Override
        public BufferedWriter call() throws Exception {
            return new BufferedWriter(writer);
        }
    };
    Callable<BufferedReader> source = new Callable<BufferedReader>() {
        @Override
        public BufferedReader call() throws Exception {
            BufferedReader reader = new BufferedReader(new StringReader(writer.toString()));
            writer = new StringWriter();
            return reader;
        }
    };

    @Before
    public void setup() {
        writer = new StringWriter();
    }

    @Test
    public void testSerialize() throws Exception {
        Child child = new Child(true);
        Parent parent = new Parent(child);
        assertEquals("{" + SerializableConfig.SERIALIZED_TYPE_KEY + "="  + Parent.class.getName() + ", aChild={" + SerializableConfig.SERIALIZED_TYPE_KEY + "=" + Child.class.getName() + ", aBoolean=true}}", SerializableConfig.serialize(parent).toString());
    }

    @Test
    public void testDeserialize() throws Exception {
        Child child = new Child(true);
        Parent parent = new Parent(child);
        Object data = SerializableConfig.serialize(parent);
        Object deserialized = SerializableConfig.deserialize(data);
        assertEquals(parent, deserialized);
    }

    @Test
    public void testSerializeUnknownObject() throws Exception {
        Unknown unknown = new Unknown();
        try {
            SerializableConfig.serialize(unknown);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void testSerializeWithFieldSerializer() {
        Comprehensive expected = new Comprehensive();
        expected.custom2.name = "aogrohjaha";
        expected.custom.data.array = new Object[] {1};
        expected.custom2.data.array = new Object[] {5,5,5};

        FieldMap fieldMap = FieldMapper.getFieldMap(Comprehensive.class);
        assertEquals(CustomSerializer.class, fieldMap.getField("custom").getSerializer().getClass());
        assertEquals(CustomSerializer2.class, fieldMap.getField("custom2").getSerializer().getClass());

        Comprehensive actual = SerializableConfig.deserializeAs(SerializableConfig.serialize(expected), Comprehensive.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeCommonTypes() {
        assertEquals(someInt, SerializableConfig.deserializeAs(SerializableConfig.serialize(someInt), int.class), 1);
        assertEquals(someLong, SerializableConfig.deserializeAs(SerializableConfig.serialize(someLong), long.class), 1);
        assertEquals(someDouble, SerializableConfig.deserializeAs(SerializableConfig.serialize(someDouble), double.class), 0.00000000001);
        assertEquals(someFloat, SerializableConfig.deserializeAs(SerializableConfig.serialize(someFloat), float.class), 0.0000000001);
        assertEquals(someShort, SerializableConfig.deserializeAs(SerializableConfig.serialize(someShort), short.class), 1);
        assertEquals(someByte, SerializableConfig.deserializeAs(SerializableConfig.serialize(someByte), byte.class), 1);
        assertEquals(someBoolean, SerializableConfig.deserializeAs(SerializableConfig.serialize(someBoolean), boolean.class));
        assertEquals(someChar, (char) SerializableConfig.deserializeAs(SerializableConfig.serialize(someChar), char.class));
        assertEquals(someBoxedInteger, SerializableConfig.deserializeAs(SerializableConfig.serialize(someBoxedInteger), Integer.class), 1);
        assertEquals(someBoxedLong, SerializableConfig.deserializeAs(SerializableConfig.serialize(someBoxedLong), Long.class), 1);
        assertEquals(someBoxedDouble, SerializableConfig.deserializeAs(SerializableConfig.serialize(someBoxedDouble), Double.class), 0.00000000001);
        assertEquals(someBoxedFloat, SerializableConfig.deserializeAs(SerializableConfig.serialize(someBoxedFloat), Float.class), 0.0000000001);
        assertEquals(someBoxedShort, SerializableConfig.deserializeAs(SerializableConfig.serialize(someBoxedShort), Short.class), 1);
        assertEquals(someBoxedByte, SerializableConfig.deserializeAs(SerializableConfig.serialize(someBoxedByte), Byte.class), 1);
        assertEquals(someBoxedBoolean, SerializableConfig.deserializeAs(SerializableConfig.serialize(someBoxedBoolean), Boolean.class));
        assertEquals(someBoxedCharacter, SerializableConfig.deserializeAs(SerializableConfig.serialize(someBoxedCharacter), Character.class));
        assertEquals(bigInteger, SerializableConfig.deserializeAs(SerializableConfig.serialize(bigInteger), BigInteger.class));
        assertEquals(bigDecimal, SerializableConfig.deserializeAs(SerializableConfig.serialize(bigDecimal), BigDecimal.class));
        assertEquals(atomicInteger.get(), SerializableConfig.deserializeAs(SerializableConfig.serialize(atomicInteger), AtomicInteger.class).get());
        assertEquals(atomicLong.get(), SerializableConfig.deserializeAs(SerializableConfig.serialize(atomicLong), AtomicLong.class).get());
        assertEquals(anum, SerializableConfig.deserializeAs(SerializableConfig.serialize(anum), Anum.class));
        assertEquals(fnum, SerializableConfig.deserializeAs(SerializableConfig.serialize(fnum), FakeEnum.class));
        assertEquals(uuid, SerializableConfig.deserializeAs(SerializableConfig.serialize(uuid), UUID.class));
        assertEquals(locale, SerializableConfig.deserializeAs(SerializableConfig.serialize(locale), Locale.class));
        assertEquals(string, SerializableConfig.deserializeAs(SerializableConfig.serialize(string), String.class));

        //List newList = (List) SerializableConfig.deserializeAs(SerializableConfig.serialize(list), LinkedList.class);
        //for (int i = 0; i < list.size(); i++) {
        //    assertEquals(list.get(i), newList.get(i));
        //}
    }

    @Test
    public void testGson() throws Exception {
        GsonDataSource.Builder builder = GsonDataSource.builder();
        DataSource dataSource = builder.setSink(sink).setSource(source).build();
        fileSerializeComprehensive(dataSource);
        //fileSerializeCommonTypes(dataSource);
    }

    @Test
    public void testJson() throws Exception {
        JsonDataSource.Builder builder = JsonDataSource.builder();
        builder.getFactory().enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        DataSource dataSource = builder.setSink(sink).setSource(source).build();
        fileSerializeComprehensive(dataSource);
        //fileSerializeCommonTypes(dataSource);
    }

    @Test
    public void testYaml() throws Exception {
        YamlDataSource.Builder builder = YamlDataSource.builder();
        DataSource dataSource = builder.setSink(sink).setSource(source).build();
        fileSerializeComprehensive(dataSource);
        //fileSerializeCommonTypes(dataSource);
    }

    @Test
    public void testHocon() throws Exception {
        HoconDataSource.Builder builder = HoconDataSource.builder();
        DataSource dataSource = builder.setSink(sink).setSource(source).build();
        fileSerializeComprehensive(dataSource);
        //fileSerializeCommonTypes(dataSource);
    }

    private Object cycleData(DataSource dataSource, Object data) throws Exception {
        System.out.println(data);
        Map<String, Object> map = new HashMap<>(1);
        map.put("data", data);
        dataSource.save(map);
        map = ((Map) dataSource.load());
        System.out.println(map);
        return map.get("data");
    }

    private void fileSerializeComprehensive(DataSource dataSource) throws Exception {
        dataSource.save(comprehensive);
        Comprehensive comp = dataSource.load(Comprehensive.class);
        assertNotSame(comprehensive, comp);
        assertEquals(comprehensive, comp);
    }

    private void fileSerializeCommonTypes(DataSource dataSource) throws Exception {

        assertEquals(someBoxedInteger, cycleData(dataSource, someBoxedInteger));
        assertEquals(someBoxedDouble, (double) cycleData(dataSource, someBoxedDouble), 0.00000000001);
        assertEquals(someBoxedLong, cycleData(dataSource, someBoxedLong));
        assertEquals(someBoxedByte, cycleData(dataSource, someBoxedByte));
        assertEquals(someBoxedShort, cycleData(dataSource, someBoxedShort));
        assertEquals(someBoxedFloat, (float) cycleData(dataSource, someBoxedFloat), 0.0000000001);
        assertEquals(someBoxedBoolean, cycleData(dataSource, someBoxedBoolean));
        assertEquals(someBoxedCharacter, cycleData(dataSource, someBoxedCharacter));
        assertEquals(bigDecimal, cycleData(dataSource, bigDecimal));
        assertEquals(bigInteger, cycleData(dataSource, bigInteger));
        assertEquals(atomicInteger.get(), ((AtomicInteger) cycleData(dataSource, atomicInteger)).get());
        assertEquals(atomicLong.get(), ((AtomicLong) cycleData(dataSource, atomicLong)).get());
        assertEquals(anum, cycleData(dataSource, anum));
        assertEquals(fnum, cycleData(dataSource, fnum));
        assertEquals(uuid, cycleData(dataSource, uuid));
        assertEquals(locale, cycleData(dataSource, locale));
        assertEquals(string, cycleData(dataSource, string));

        List newList = (List) cycleData(dataSource, list);
        assertEquals(list.size(), newList.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).getClass() + " vs " + newList.get(i).getClass());
            assertEquals(list.get(i), newList.get(i));
        }
    }

    @Test
    public void testMapNullContainer() throws Exception {
        NullContainer expected = new NullContainer();
        expected.parent = Comprehensive.PARENT;
        NullContainer actual = new NullContainer();
        FieldMapper.mapFields(expected, actual);
        assertEquals(expected, actual);
    }
}
