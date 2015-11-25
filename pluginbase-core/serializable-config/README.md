## Serializable-Config
### Description
Serializable-Config is PluginBase's solution to the storage of configuration and data objects. It aims to be easy to use while providing a large feature set geared towards a higher quality end user experience.
### Main Features
* Automagic Serialization - just pass it the objects you want serialized or the data you want deserialized.
* File Comments - comments for key:value pairs are available for file types that support it (check out the Hocon option).
* Stringy Properties - Your configuration/data can be treated as a string:string data set, which allows end users to easily edit in app.
* Custom Serializers - In case the automagic kind isn't versatile enough.

### Basic Usage
You have 2 main options for using Serializable-Config.
#### Handle the files yourself
If you would like to handle the files yourself, you can access the serialization features of Serializable-Config in the [SerializableConfig](http://ci.onarandombox.com/job/PluginBase/javadoc/pluginbase/config/SerializableConfig.html) class.
It is generally as simple as calling ``SerializableConfig.serialize(someObject);`` to serialize the object and ``SerializableConfig.deserialize(serializedData);`` to deserialize data back into the object.
#### Let Serializable-Config handle the files
Serializable-Config combines with [Configurate](https://github.com/zml2008/configurate) in order to provide serialization directly to Hocon, JSON, GSON, and YAML files. Using this method will likely require additional dependencies in order to function, as noted in the [javadoc](http://ci.onarandombox.com/job/PluginBase/javadoc/).
Hocon is the recommended file type as it is a very human readable superset of JSON that supports file comments.
Here is a sample of how you would save and load your object to a Hocon [data source](http://ci.onarandombox.com/job/PluginBase/javadoc/pluginbase/config/datasource/DataSource.html):
```Java
DataSource dataSource = HoconDataSource.builder().setFile(someFile).build();
dataSource.save(someObject);
// Do other stuff...
SomeObject myObject = dataSource.load();
```
### Advanced Usage
For advanced usage refer to the ``pluginbase.config`` package in the [javadoc](http://ci.onarandombox.com/job/PluginBase/javadoc/)