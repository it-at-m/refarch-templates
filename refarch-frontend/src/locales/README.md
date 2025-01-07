# Locales Directory

This directory contains all the translation files for the project. Below is a recommended structure and set of conventions to help keep translations organized, consistent, and easy to maintain.

## Key Naming Conventions

1. **camelCase Keys**

   - Use lowerCamelCase for individual keys, e.g. `home.header`, `app.name.full`.
   - This convention aligns with lint rules and keeps keys visually distinct from normal text.

2. **Hierarchical Grouping**

   - Organize keys by feature or component, using multiple nested levels for clarity.
   - Example:
     ```yaml
     home:
       header: "Welcome!"
       paragraph: "Some text here..."
     ```

3. **Consistent Section Prefixes**
   - Maintain distinct top-level sections: `common`, `app`, `views`, `components`, `domain`, etc.
   - This consistency makes it easier to quickly locate strings in a large codebase.

## Special Character Handling

- When using special or reserved YAML characters (e.g., `@`, `'`, `"`, `{`), ensure they are properly quoted if needed.
- Complex strings may require double quotes to avoid parsing or lint errors, for example:
  ```yaml
  name:
    full: "@:app.name.part1@:app.name.part2"
  ```
- If you need placeholders in strings, consider using interpolation features offered by vue-i18n (e.g., `{item} gespeichert.`).

## Section Organization Guidelines

1. common

   - For frequent, repeated terms like “yes”/“no” or action verbs like “close,” “save,” “cancel.”

2. app

   - For global application-related strings (e.g., brand name, headers).

3. views

   - For text used within specific pages/views (e.g., views.home.header).

4. components

   - For text that appears in reusable UI components.

5. domain

   - For translating domain-specific objects, like database fields or business entities.

## Example Directory Structure

```
locales/
├── en.yaml
├── de.yaml
└── README.md
```

Each YAML file should follow the same organizational approach. Developers can add more locale files as needed (e.g., es.yaml, fr.yaml) to accommodate additional languages.

## Additional Tips

- Keep an eye out for unused keys: remove them to maintain clarity and reduce confusion.
- Document any non-obvious translations or placeholders in comment blocks.
- If you have an automated linter check (e.g., @intlify/vue-i18n/no-unused-keys or @intlify/vue-i18n/key-format-style), ensure your keys stay in sync with usage in the code.
