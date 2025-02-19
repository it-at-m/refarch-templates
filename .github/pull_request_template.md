# Pull Request

<!-- Links -->
[documentation-link]: https://refarch-templates.oss.muenchen.de/document.html#writing-the-documentation
[code-quality-link]: https://refarch-templates.oss.muenchen.de/develop.html#code-quality
[refarch-create-issue-link]: https://github.com/it-at-m/refarch/issues/new/choose

## Changes

- ...
- ...

## Reference

Issue: #XXX

## Checklist

**Note**: If some checklist items are not relevant for your PR, just remove them.

### General

- [ ] ~~I have read the Contribution Guidelines (TBD)~~
- [ ] Met all acceptance criteria of the issue
- [ ] Added meaningful PR title and list of changes in the description
- [ ] Created / Updated [documentation][documentation-link] (in English)
- [ ] Opened follow-up issue in [refarch repository][refarch-create-issue-link] (if applicable)

### Code

- [ ] Wrote code and comments in English
- [ ] Added unit tests
- [ ] Removed waste on branch (e.g. `console.log`), see [code quality tooling][code-quality-link]

#### Frontend / WebComponent

- [ ] Added component tests (if component was changed)
- [ ] Considered and tested accessibility (if UI change was made)
- [ ] Checked / Updated Node.js engine requirements in `package.json` (if dependencies were changed)

#### Backend / EAI

- [ ] Added integration tests
- [ ] Updated database migration scripts (if changes to model were made)
- [ ] Added Swagger API annotations (if changes to API was made)
- [ ] Checked Spring Boot version matching Camel version in `pom.xml` (if Camel version was bumped)

### Development Stack

- [ ] Approved image change company internal (if Docker image was added or version was modified)
- [ ] Checked functionality of Docker stack (if Docker stack was modified or images were changed)

## Screenshots (if UI was changed)