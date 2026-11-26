"""Generate the selected modules of a project. Runs inside the generated project."""

import os
from pathlib import Path
import re
import subprocess
import sys

template, src_path, project_name, project_slug, modules = sys.argv[1:6]

for module in filter(None, modules.split(",")):
    subprocess.run(
        [
            sys.executable, "-m", "copier", "copy", "--trust",
            # set by .copier/scripts/test unless it runs with -i
            *(["--defaults"] if os.environ.get("TEST_REFARCH_COPIER_DEFAULTS") else []),
            "-d", f"module_type={module}",
            "-d", f"project_name={project_name}",
            "-d", f"project_slug={project_slug}",
            template, module,
        ],
        check=True,
    )
    # Copier recorded its temporary clone; point the module at the real template so `copier update` works.
    answers = Path(module, ".copier-answers.yml")
    answers.write_text(
        re.sub(r"^_src_path: .*$", lambda _: f"_src_path: {src_path}", answers.read_text(), flags=re.M)
    )
