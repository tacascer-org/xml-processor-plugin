# XML Processor Plugin

This plugins packages the [XML Processor](https://github.com/tacascer-org/xml-processor?tab=readme-ov-file#xml-processor) as a Gradle plugin.

## Usage

To use the XML Processor Plugin, you need to apply it in your `build.gradle.kts` file and configure the `xmlProcessor`
extension as needed. Here's an example:

```kotlin
plugins {
    id("io.github.tacascer.xml-processor")
}

xmlProcessor {
    flatten {
        inputFile.set(file("path/to/your/input.xml"))
        outputFile.set(file("path/to/your/output.xml"))
    }
}
```

Then, you can run the `xmlFlatten` task to flatten your XML schema:

```bash
./gradlew xmlFlatten
```

### Example

Consider the following scenario: you have an XML schema that includes another schema, and you want to flatten it into a
single schema. Here's how you can do it:

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
        flatten {
            inputFile.set(file("sample.xsd"))
            outputFile.set(file("output.xsd"))
        }
    }
    ```

3. Run the `xmlFlatten` task:

    ```bash
    ./gradlew xmlFlatten
    ```

4. The `output.xsd` file will now contain a flattened version of your XML schema.

## Contributing

If you want to contribute to the XML Processor Plugin, please create an issue or open a pull request.

## License

Please see the `LICENSE` file for details about the license.
