"""Generate the selected modules of a project. Runs inside the generated project."""

import os
import subprocess
import sys

template, project_name, project_slug, modules = sys.argv[1:5]

for module in filter(None, modules.split(",")):
    subprocess.run(
        [
            sys.executable, "-m", "copier", "copy", "--trust",
            # set by .copier/scripts/test unless it runs with -i
            *(["--defaults"] if os.environ.get("TEST_REFARCH_COPIER_DEFAULTS") else []),
            "-d", f"module_type={module}",
            "-d", f"project_name={project_name}",
            "-d", f"project_slug={project_slug}",
            "-d", "standalone=false",
            template, module,
        ],
        check=True,
    )
