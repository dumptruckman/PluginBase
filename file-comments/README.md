This module allows you to add comments to your configuration files.

Currently only YAML files are supported.

Simple usage:
```java
// Create a comment instrumenter that uses indent length of 2
YamlFileCommentInstrumenter commentInstrumenter = YamlFileCommentInstrumenter.createCommentInstrumenter(2);

// Add some comments
commentInstrumenter.setCommentsForPath("my.awesome.path", "Some awesome comments.");
commentInstrumenter.setCommentsForPath("another.path", "This is a", "multi-line comment!");

// Write the comments to a yaml file
commentInstrumenter.saveCommentsToFile(new File("config.yml"));
```

Keep in mind that this process involves a good deal of string parsing and as such is not a particularly high performance operation.

------------------
PluginBase License
------------------

The following license applies to this and all sub modules of PluginBase:

Copyright (C) dumptruckman 2013

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.