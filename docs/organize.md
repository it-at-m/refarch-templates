# Organize

The following explains the currently used
practices and recommendations for organizing
your projects in GitHub.


## Labeling

Labels are used for various purposes. Each project uses
different labels, such as `frontend`, to help
differentiate them.

Additionally, labels indicate the priority of
issues, for example, `Medium`. There are also labels
that specify the type of issue, like
`Documentation`.

We highly recommend using labels to maintain a
clear structure. You can view all current labels
[here](https://github.com/it-at-m/refarch-templates/labels).

These labels are also utilized for Automatic
Labeling, which will be explained later.


## Issue Templates

Issue templates are essential for the project.
They streamline the process of reporting bugs,
requesting features, or providing feedback.

These templates guide contributors in sharing
relevant information, ensuring clarity and
improving communication. Using them enhances
collaboration and creates a more efficient
development process.

Currently, there are four types of issue templates
available for use. You can find the code for these
templates in the `.github/ISSUE_TEMPLATE` folder.

If needed, you can extend or remove any templates.
Out of the box, issues created with one of the
templates will be automatically labeled.


### Bug Report

Report a bug to help the project improve.

### Feature request

Suggest an idea or possible new feature for this project.

### Maintenance work

Suggest useful or required maintenance work for this project.

### Documentation change

Suggest a documentation change for this project.

## GitHub Projects

For the Refarch, we currently use
[GitHub Projects](https://docs.github.com/de/issues/planning-and-tracking-with-projects/learning-about-projects/about-projects)
for project management.

It is best suited for smaller projects that do
not require extensive project management and
have fewer stakeholders.

If you have a larger project with more
stakeholders, we recommend using more
comprehensive solutions like Jira.


## Code of Conduct

The Code of Conduct is a crucial part of our
project's documentation. It outlines expected
behaviors to ensure a respectful and inclusive
environment for all contributors.

This document promotes collaboration by
addressing harassment and discrimination,
fostering a culture where everyone feels safe
and valued.

By adhering to this Code, we enhance our
collective efforts and encourage diverse
perspectives.

## License

The license included in our GitHub project is a
legal document that defines how the project's code
and assets can be used, modified, and shared.

It clarifies the rights and responsibilities of
both contributors and users, promoting transparency
and encouraging collaboration.

By specifying usage terms, the license protects
the authors' intellectual property while fostering
an open-source culture. This ensures that everyone
understands how to engage with the project
responsibly.

By default, all LHM projects are licensed under
MIT.


## Welcome Bot
For new Contributors, that create their first Issue, Open there First
PR or Merge their first Pr, we use a welcome bot To appreciate the
Contribution that are doing. The config for the Bot is inside
the `.github/config.yml` and can be changed if wanted.

The Bot is not enabled per default for a new project. To Enable it you need
to go to the [github site](https://probot.github.io/apps/welcome/) and press
the `+ Add to Github` button, afterwards your need to select your project to add it.

## Automatic Labeling

To automatically label pull requests, the project
uses a [Labeler Action](https://github.com/actions/labeler)
with custom configuration in `.github/labeler.yml`.

Labeling depends on the branch prefix and your
branch name. For example, the following line:


```
"Type: Feature":
  - head-branch: [ '^feat', '-feature-' ]
```

will add a label to a pull request if the branch
name has `feat` as a prefix, e.g., `feat/001-ticket`.

It will also add a label based on which files were
changed, which is useful for multi-project repos.



```
"Template: EAI":
  - changed-files:
      - any-glob-to-any-file: ['refarch-eai/**']
```

This will add an `EAI` label if changes in the EAI
folder have occurred.





