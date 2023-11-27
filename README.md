# SC2002 AY23/24 Sem 1 - OO Design & Programming Assignment

## JAR
The compiled JAR is provided as `CAMs.jar`

```shell
java -jar CAMs.jar
```

## Setup

### Maven Dependencies
- [OpenCSV](https://mvnrepository.com/artifact/com.opencsv/opencsv)
```
<dependency>
    <groupId>com.opencsv</groupId>
    <artifactId>opencsv</artifactId>
    <version>5.7.1</version>
</dependency>
```
- [Apache Commons Codec](https://mvnrepository.com/artifact/commons-codec/commons-codec)
```
<dependency>
    <groupId>commons-codec</groupId>
    <artifactId>commons-codec</artifactId>
    <version>1.15</version>
</dependency>
```
### Directory & File Structure
resources (./data) and output (./report_by_*) directories must have the following structure.

`root`:
```
├─── data
│    │ keys.csv
│    │ users.csv
│    │
│    └─── camp
│         │ dates.csv
│         │ enquiries.csv
│         │ slots.csv
│         │ suggestions.csv
│
├─── report_by_committee
├─── report_by_staff
```
