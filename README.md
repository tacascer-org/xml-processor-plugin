# XML Processor Plugin

[![Gradle Plugin Portal Version](https://img.shields.io/gradle-plugin-portal/v/io.github.tacascer.xml-processor?style=for-the-badge&logo=gradle)](https://plugins.gradle.org/plugin/io.github.tacascer.xml-processor)

![Build](https://github.com/tacascer-org/xml-processor/actions/workflows/build.yml/badge.svg?branch=main)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=tacascer-org_xml-processor-plugin&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=tacascer-org_xml-processor-plugin)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=tacascer-org_xml-processor-plugin&metric=coverage)](https://sonarcloud.io/summary/new_code?id=tacascer-org_xml-processor-plugin)

This plugin packages the [XML Processor](https://github.com/tacascer-org/xml-processor?tab=readme-ov-file#xml-processor)
as a Gradle plugin.

## Usage

To use the XML Processor Plugin, you need to apply it in your `build.gradle.kts` file and configure the `xmlProcessor`
extension as needed. Here's an example:

```kotlin
plugins {
    id("io.github.tacascer.xml-processor")
}

xmlProcessor {
    // configuration here
}
```

### Example 1: Flatten XML Schema

1. Create your input XML files:

   `sample.xsd`:

    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" targetNamespace="http://www.sample.com">
        <xs:include schemaLocation="sample_1.xsd"/>
        <xs:element name="sample" type="xs:string"/>
    </xs:schema>
    ```

   `sample_1.xsd`:

    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema">
        <xs:element name="sampleOne" type="xs:string"/>
    </xs:schema>
    ```

2. Configure the `xmlProcessor` extension in your `build.gradle.kts` file:

    ```kotlin
    xmlProcessor {
        processingSets {
            register("flatten") {
                input.add(file("path/to/your/sample.xsd"))
                output.set(layout.buildDirectory.dir("output"))
                filters.add(io.github.tacascer.flatten.IncludeFlattener())
            }
        }
    }
    ```

   This will create a task named `processXmlSetFlatten`.

3. Run the task:

    ```bash
    ./gradlew processXmlSetFlatten
    ```

4. The `build/output/sample.xsd` file will now contain a flattened version of your XML schema

      ```xml
     <?xml version="1.0" encoding="UTF-8"?>
     <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" targetNamespace="http://www.sample.com">
         <xs:element name="sample" type="xs:string" />
         <xs:element name="sampleOne" type="xs:string" />
     </xs:schema>
      ```

## Example 2: Defining multiple processing sets

You can define multiple processing sets, like so:

`build.gradle.kts`:

```kotlin
xmlProcessor {
    processingSets {
        register("flatten") {
            input.add(file("path/to/your/sample.xsd"))
            output.set(layout.buildDirectory.dir("output"))
            filters.add(io.github.tacascer.flatten.IncludeFlattener())
        }
        register("strip") {
            input.add(file("path/to/your/sample.xsd"))
            output.set(layout.buildDirectory.dir("output"))
            filters.add(io.github.tacascer.namespace.NamespaceRemover())
        }
    }
}
```

This will create 2 tasks: `processXmlSetFlatten` and `processXmlSetStrip`.
