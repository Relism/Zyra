# Zyra <img src="zyra_nobg.png" align="left" height="120px" alt="Zyra logo" />

Zyra is a Java annotation-based code generation tool for producing type-safe TypeScript definitions and Zod schemas from your POJOs.

---

## Why do i need it?

Zyra simplifies full-stack development by allowing backend developers to define their data models once in Java and automatically generate type-safe TypeScript and Zod schema representations for use in the frontend. No more maintaining duplicate model definitions across layers — Zyra does it for you, cleanly and reliably.

---

## Real-World Example Output

Given the annotated Java class:

```java
@Zyra(
    export = Zyra.Export.DEFAULT,
    style = Zyra.DefinitionStyle.INTERFACE,
    indentSpaces = 2
)
public class User {
    private String username;
    private int age;
    private boolean active;
    private Address address;
    private List<String> roles;
    private List<User> friends;
    private Map<String, Integer> preferences;
    private Status status;
}
```

Zyra generates the following TypeScript definition:

```ts
import { Address, Status } from './';

export default interface User {
    username: string;
    age: number;
    active: boolean;
    address: Address;
    roles: string[];
    friends: User[];
    preferences: { [key: string]: number };
    status: Status;
}
```

---

## ⚙️ Configuration Options

The `@Zyra` annotation supports the following options:

| Option             | Description                                                                 |
|--------------------|-----------------------------------------------------------------------------|
| `export`           | Controls the export style. Can be `DEFAULT`, `NAMED`, or `NULL` (no export). |
| `style`            | Generates either a TypeScript `INTERFACE` or a `TYPE` alias.                |
| `indentSpaces`     | Sets indentation width (e.g. `2`, `4`, etc.).                               |
| `filenameOverride` | Overrides the output filename (e.g. `UserDto.ts` instead of `User.ts`).     |

---

## 📦 Features

- ✅ Supports all Java primitives and collections (`List`, `Map`, etc.)
- ✅ Recursively generates definitions for custom nested classes (like `Address`), as long as they are also annotated with `@Zyra`
- ✅ Automatically generates `import` statements for referenced types within the same Zyra generation scope
- ✅ Clean and customizable output with consistent formatting

---

