# Organize

The following explains the currently used practices and recommendations for organizing your projects in GitHub.

::: danger IMPORTANT
Please make sure you worked through the corresponding [Getting Started](./getting-started#documentation) instructions before proceeding.
:::

## Labeling

Labels are used for various purposes.
Each project uses different labels, such as `frontend`, to help differentiate them.

Additionally, labels indicate the priority of issues, for example, `Medium`.
There are also labels that specify the type of issue, like `Documentation`.

We highly recommend using labels to maintain a clear structure.
You can view all our current labels [here](https://github.com/it-at-m/refarch-templates/labels) for inspiration.
For more information on how to set up labels for your own project, please check the official [GitHub documentation](https://docs.github.com/en/issues/using-labels-and-milestones-to-track-work/managing-labels).

Your project labels are also utilized for [Automatic Labeling](#automatic-labeling).

## Issue Templates

Issue templates are essential for the project.
They streamline the process of reporting bugs, requesting features, or providing feedback.

These templates guide contributors in sharing relevant information, ensuring clarity and improving communication.
Using them enhances collaboration and creates a more efficient development process.

Currently, there are four types of issue templates available for use.
You can find the code for these templates in the `.github/ISSUE_TEMPLATE` folder.

If needed, you can extend or remove any templates.
Out of the box, issues created with one of the templates will be automatically labeled.

- **Bug Report**: Report a bug to help the project improve.
- **Feature Request**: Suggest an idea or possible new feature for this project.
- **Maintenance Work**: Suggest useful or required maintenance work for this project.
- **Documentation Change**: Suggest a documentation change for this project.

Please check the official [GitHub documentation](https://docs.github.com/en/communities/using-templates-to-encourage-useful-issues-and-pull-requests/syntax-for-issue-forms) for further information.

::: danger IMPORTANT
Issue forms are currently a GitHub preview feature and thus subject to change in the future.
:::

## Pull Request Template

The template provides a pull request template (in `.github/pull_request_template.md`) containing a checklist of tasks that should be fulfilled by the PR creator.
This ensures a uniform review process, simplifies organization, and allows maintaining a high code quality.

Please check the official [GitHub documentation](https://docs.github.com/en/communities/using-templates-to-encourage-useful-issues-and-pull-requests/creating-a-pull-request-template-for-your-repository) for further information.

In combination with [Require PR checklist](./develop.html#require-pr-checklist) the templates can enforce a careful examination of each PR checklist item.

::: info Information
Note that not all checkpoint items might be relevant for your project, so you have to adjust the template to your own needs.
:::

## GitHub Projects

For the RefArch, we currently use [GitHub Projects](https://docs.github.com/en/issues/planning-and-tracking-with-projects/learning-about-projects/about-projects) for project management.

It is best suited for smaller projects that do not require extensive project management and have fewer stakeholders.

If you have a larger project with more stakeholders, we recommend using more comprehensive solutions like Jira.

## Code of Conduct

The Code of Conduct (which can be found in `.github/CODE_OF_CONDUCT.md`) outlines expected behaviors to ensure a respectful and inclusive environment for all contributors.

This document promotes collaboration by addressing harassment and discrimination, fostering a culture where everyone feels safe and valued.

By adhering to this Code, we enhance our collective efforts and encourage diverse perspectives.

More information about the Code of Conduct can be found in the official [GitHub documentation](https://docs.github.com/en/communities/setting-up-your-project-for-healthy-contributions/adding-a-code-of-conduct-to-your-project).

## License

The license included in our GitHub project is a legal document that defines how the project's code and assets can be used, modified, and shared.

It clarifies the rights and responsibilities of both contributors and users, promoting transparency and encouraging collaboration.

By specifying usage terms, the license protects the authors' intellectual property while fostering an open-source culture.
This ensures that everyone understands how to engage with the project responsibly.

By default, all LHM projects are licensed under [MIT](https://choosealicense.com/licenses/mit/).

## Welcome Bot

For new contributors who create their first issue, open their first PR, or merge their first PR, we use a welcome bot to appreciate their contributions.
The configuration for the bot is inside `.github/config.yml` and can be modified if needed.

The bot is not enabled by default for new projects.
To enable it, you need to go to the [GitHub site](https://probot.github.io/apps/welcome/) and press the `+ Add to GitHub` button. Afterward, select your project to add it.

## Automatic Labeling

To automatically label pull requests, the template provides out-of-the-box configuration for a [Labeler Action](https://github.com/actions/labeler) through the workflow file in `.github/workflows/pr-labeler.yml` with custom configuration in `.github/labeler.yml`.
Make sure to adjust the configuration file to your specific project needs.

Labeling depends on the branch prefix or your branch name.
For example, the following line:

```yml
"Type: Feature":
  - head-branch: ["^feat", "-feature-"]
```

will add a label to a pull request if the branch name has `feat` as a prefix, e.g., `feat/001-ticket`, or if you created the branch through the corresponding issue template.

It will also add a label based on which files were changed, which is useful for multi-project repos.

```yml
"Template: EAI":
  - changed-files:
      - any-glob-to-any-file: ["refarch-eai/**"]
```

This will add an `Template: EAI` label if changes in the `refarch-eai` folder have occurred.
