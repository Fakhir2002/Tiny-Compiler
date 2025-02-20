# Custom Programming Language - README

## File Extension

All programs written in this language should use the `.urdu` file extension.

## Overview

This document provides a comprehensive guide to the syntax, keywords, rules, and error handling mechanisms for the custom programming language implemented in this project.

## Keywords and Their Meanings

| Keyword      | Meaning                                   |
| ------------ | ----------------------------------------- |
| `sachai`     | Boolean Data Type (True/False)            |
| `sahi`       | Represents `true` (Stored as `1`)         |
| `jhoot`      | Represents `false` (Stored as `0`)        |
| `adad`       | Integer Data Type                         |
| `nukta`      | Decimal Data Type (Floating Point)        |
| `harf`       | Character Data Type                       |
| `jumla`      | String Data Type                          |
| `duniyawala` | Global Scope Declaration                  |
| `gharwala`   | Local Scope Declaration                   |
| `muqarrar`   | Constant Declaration (Immutable Variable) |

## Data Types

| Data Type          | Description                                            | Example                     |
| ------------------ | ------------------------------------------------------ | --------------------------- |
| `sachai` (Boolean) | True/False values, stored as `1` (sahi) or `0` (jhoot) | `sachai is_valid = sahi;`   |
| `adad` (Integer)   | Whole numbers                                          | `adad x = 10;`              |
| `nukta` (Decimal)  | Floating point numbers (up to 5 decimal places)        | `nukta pi = 3.14159;`       |
| `harf` (Character) | Single character enclosed in single quotes             | `harf letter = 'a';`        |
| `jumla` (String)   | Text enclosed in double quotes                         | `jumla greeting = "hello";` |

## Operators

| Operator | Meaning             | Example                   |
| -------- | ------------------- | ------------------------- |
| `+`      | Addition            | `adad sum = x + y;`       |
| `-`      | Subtraction         | `adad diff = x - y;`      |
| `*`      | Multiplication      | `adad product = x * y;`   |
| `/`      | Division            | `nukta result = x / y;`   |
| `%`      | Modulus (Remainder) | `adad remainder = x % 5;` |
| `=`      | Assignment          | `adad num = 5;`           |
| `^`      | Exponentiation      | `nukta power = x ^ y;`    |

## Scope Rules

- Variables can be declared **globally** (`duniyawala`) or **locally** (`gharwala`).
- Global variables persist throughout the program, while local variables exist only within a block.

```plaintext
  duniyawala adad globalVar = 50;
  gharwala adad localVar = 10;
```

## Comments

- **Single-line comments** start with `~`.
- **Multi-line comments** are enclosed between `~~ ... ~~`.

```plaintext
~ This is a single-line comment

~~
  This is a
  multi-line comment
~~
```

## Syntax Rules

- **Only lowercase letters are allowed** in variable names (`a-z`).
- **Boolean values** must be `sahi` (true) or `jhoot` (false).
- **Strings** must be enclosed in `"double quotes"`.
- **Characters** must be enclosed in `'single quotes'`.
- **Decimals** support up to 5 decimal places.
- **Constants (`muqarrar`) cannot be reassigned**.

```plaintext
muqarrar adad max_limit = 100;
max_limit = 200; ~ ERROR: Cannot modify constant
```

## Error Handling Rules

### Uppercase Letters Are Not Allowed

- **Identifiers must be in lowercase**.
- Example: `adad WrongVar = 50;` will cause an **error**.

### Undeclared Variable Usage

- Using a variable before declaring it results in an error.
- Example:

```plaintext
x = 10;  ~ ERROR: Variable 'x' is not declared before assignment.
```

### Invalid Boolean Assignment

- Boolean values must be `sahi` or `jhoot`.
- Example:

```plaintext
sachai flag = maybe;  ~ ERROR: 'maybe' is not a valid Boolean value.
```

### Constants Cannot Be Modified

- Reassigning a constant declared with `muqarrar` results in an error.
- Example:

```plaintext
muqarrar nukta gravity = 9.81;
gravity = 10.2;  ~ ERROR: Cannot modify constant 'gravity'.
```

### Assignment Without Identifier

- Assigning a value without declaring a variable causes an error.
- Example:

```plaintext
= 100;  ~ ERROR: Assignment without a valid identifier.
```

## Example Program

```plaintext
sachai is_valid = sahi;
adad number = 42;
nukta pi = 3.14159;
harf letter = 'a';
jumla greeting = "hello";
muqarrar adad max_limit = 100;
~ Single-line comment
~~ Multi-line comment ~~
number = 20;  ~ Valid reassignment
max_limit = 200;  ~ ERROR: Cannot modify constant
```

## Summary

- Supports Boolean, Integer, Decimal, Character, and String types
- Uses keywords in Urdu-inspired syntax
- Enforces lowercase-only variable names
- Supports single-line and multi-line comments
- Implements strict error handling
- Includes arithmetic operators and exponentiation
- Prevents modification of constants
- Allows global and local scope management

## Compiler Implementation Details

The compiler has been developed in multiple phases:

1. **Regular Expressions to Automata**: Implemented **Thompson’s Construction** to convert **Regular Expressions** into **NFAs** and then **determinized** them into **DFAs**.
2. **Lexical Analysis**: A **DFAScanner** tokenizes the source code, identifying keywords, identifiers, operators, and literals while enforcing syntax rules.
3. **Symbol Table Management**: A **SymbolTableProcessor** keeps track of declared variables, their types, scope (Global/Local), and values.
4. **Error Handling**: Implemented a structured **ErrorHandler** to track lexical and semantic errors such as invalid identifiers, reassignment of constants, and undeclared variables.

Further phases will include **parsing, semantic analysis, and code generation** to make this a fully functional compiler.
