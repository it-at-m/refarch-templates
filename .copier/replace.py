from pathlib import Path
import re
import shutil
import sys

template_type, java_package, project_slug = sys.argv[1:4]

OLD_GROUP = "de.muenchen.oss.refarch"
old_package = f"{OLD_GROUP}.{template_type}"
new_group = java_package.rsplit(".", 1)[0]

# Full package only: the bare OLD_GROUP is also the groupId of the external refarch-pmd dependency.
for path in Path("src").rglob("*"):
    if path.is_file() and path.suffix in {".java", ".yml", ".yaml", ".properties", ".xml"}:
        text = path.read_text()
        if old_package in text:
            path.write_text(text.replace(old_package, java_package))

for yml in Path("src/main/resources").glob("application*.yml"):
    yml.write_text(yml.read_text().replace(f"{OLD_GROUP}:", f"{new_group}:"))

# Only the project's own groupId, which is directly followed by its artifactId.
pom = Path("pom.xml")
text, count = re.subn(
    rf"<groupId>{re.escape(OLD_GROUP)}</groupId>(\s*)<artifactId>refarch-{template_type}</artifactId>",
    rf"<groupId>{new_group}</groupId>\1<artifactId>{project_slug}-{template_type}</artifactId>",
    pom.read_text(),
    count=1,
)
if count != 1:
    sys.exit("replace.py: project groupId not found in pom.xml")
pom.write_text(text.replace(f"<name>refarch-{template_type}</name>", f"<name>{project_slug}-{template_type}</name>", 1))

for root in (Path("src/main/java"), Path("src/test/java")):
    old_dir = root / old_package.replace(".", "/")
    new_dir = root / java_package.replace(".", "/")
    if not old_dir.exists() or old_dir == new_dir:
        continue
    new_dir.parent.mkdir(parents=True, exist_ok=True)
    shutil.move(old_dir, new_dir)
    # remove the now empty de/muenchen/... folders
    for parent in old_dir.parents:
        if parent == root or any(parent.iterdir()):
            break
        parent.rmdir()
