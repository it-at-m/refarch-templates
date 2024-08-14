import fs = require("fs");
import {replaceInFile} from "replace-in-file";
import {checkbox, input} from "@inquirer/prompts";


startpoint();
async function startpoint() {
    const FRONTEND = "frontend";
    const BACKEND = "backend";
    const EAI = "eai";
    await checkbox({
        message: 'Select Project/s you want to generate with space',
        choices: [
            {name: FRONTEND, value: FRONTEND},
            {name: BACKEND, value: BACKEND},
            {name: EAI, value: EAI},
        ],
        required: true,
    }).then((result: string[]) => {
        if (result.includes(BACKEND)) {
            generateBackend();
        }
    });
}

async function generateBackend() {
    const groupId = await input({
        message: "Define value for property groupId (should match expression '^de\\.muenchen\\.[a-z0-9]+(\\.[a-z0-9]+)*$'): ",
        validate(value) {
            const pass = value.match(/^de\.muenchen\.[a-z0-9]+(\.[a-z0-9]+)*$/g);
            return pass ? true : "GroupId name not valid";
        },
        required: true,
    });
    const artifactId = await input({message: "Define value for property artifactId:",  required: true,});
    const packageName = await input({message: "Define value for property package:",
        default: groupId,
        validate(value) {
            const pass = value.match(/^de\.muenchen\.[a-z0-9]+(\.[a-z0-9]+)*$/g);
            return pass ? true : "Package name not valid";
        },
        required: true,
    });
    const projectName = await input({message: "Define value for Project Name:",  required: true});
    const replacements = [{
        files: "../refarch-backend/src/main/java/de/muenchen/refarch/**/*.java",
        from: [/package de.muenchen.refarch/g, /import de.muenchen.refarch/g],
        to: [`package ${packageName}`, `import ${packageName}`],
        dry: true,
        countMatches: true,
    }, {
        files: "../refarch-backend/pom.xml",
        from: ["<groupId>de.muenchen.refarch</groupId>", "<artifactId>refarch-backend</artifactId>", "<name>refarch_backend</name>"],
        to: [`<groupId>${groupId}</groupId>`, `<artifactId>${artifactId}</artifactId>`, `<name>${artifactId}</name>`],
        dry: true,
        countMatches: true,
    }]
    Promise.all(
        replacements.map(options => replaceInFile(options))
    ).then(result => {
        console.log(result)
        fs.rename("../refarch-backend", `../${projectName}`, () => {});
    });
}