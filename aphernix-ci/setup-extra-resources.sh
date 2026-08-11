#!/usr/bin/env bash
set -euo pipefail

mkdir -p \
  src/main/resources/assets/aphernix/lang \
  src/main/resources/assets/aphernix/models/item \
  src/main/resources/assets/aphernix/textures/item \
  src/main/resources/data/aphernix/recipes

cat > src/main/resources/assets/aphernix/lang/en_us.json <<'EOF'
{
  "entity.aphernix.aphernix": "Aphernix",
  "item.aphernix.aphernix_spawn_egg": "Aphernix Spawn Egg",
  "item.aphernix.ender_eye_dust": "Eye of Ender Dust",
  "item.aphernix.ender_bite": "Ender Bite",
  "message.aphernix.called": "Aphernix came to you!",
  "message.aphernix.no_aphernix": "No Aphernix was found nearby."
}
EOF

cat > src/main/resources/assets/aphernix/lang/tr_tr.json <<'EOF'
{
  "entity.aphernix.aphernix": "Aphernix",
  "item.aphernix.aphernix_spawn_egg": "Aphernix Çağırma Yumurtası",
  "item.aphernix.ender_eye_dust": "Ender Gözü Tozu",
  "item.aphernix.ender_bite": "Ender Bite",
  "message.aphernix.called": "Aphernix yanına geldi!",
  "message.aphernix.no_aphernix": "Yakınlarda Aphernix bulunamadı."
}
EOF

cat > src/main/resources/assets/aphernix/models/item/ender_eye_dust.json <<'EOF'
{"parent":"minecraft:item/generated","textures":{"layer0":"aphernix:item/ender_eye_dust"}}
EOF

cat > src/main/resources/assets/aphernix/models/item/ender_bite.json <<'EOF'
{"parent":"minecraft:item/generated","textures":{"layer0":"aphernix:item/ender_bite"}}
EOF

cat > src/main/resources/data/aphernix/recipes/ender_eye_dust.json <<'EOF'
{
  "type": "minecraft:crafting_shapeless",
  "category": "misc",
  "ingredients": [
    {"item": "minecraft:ender_eye"}
  ],
  "result": {
    "item": "aphernix:ender_eye_dust",
    "count": 4
  }
}
EOF

cat > src/main/resources/data/aphernix/recipes/ender_bite.json <<'EOF'
{
  "type": "minecraft:crafting_shaped",
  "category": "equipment",
  "pattern": [
    " D ",
    "DPD",
    " S "
  ],
  "key": {
    "D": {"item": "aphernix:ender_eye_dust"},
    "P": {"item": "minecraft:ender_pearl"},
    "S": {"item": "minecraft:stick"}
  },
  "result": {
    "item": "aphernix:ender_bite",
    "count": 1
  }
}
EOF

base64 -d > src/main/resources/assets/aphernix/textures/item/ender_eye_dust.png <<'EOF'
iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAvUlEQVR4nGNgGAVkgab/z/6TrfnS/1//KTYEWTMjPoUxj8/9Z2BgYFAMvMrAwMDA0HwmFkM9Ey7NkQoVGE60WLsYQwzDxFoTiKI7byC2RtukMDAwMDC0BB6HqzkRjHAJTgMYGBgY7q/XZkD2wu5KBgYVM4jYElkjRqxegNm8u5KB4c6pqwx/bVfBDYNpxukFC5EASKDxaDAwMDAwqIhANOx+sJaBgYGBwVUhGG7B8gcd2F1ACKiIaMM1Dw4AAEw9QF9WynE1AAAAAElFTkSuQmCC
EOF

base64 -d > src/main/resources/assets/aphernix/textures/item/ender_bite.png <<'EOF'
iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAiUlEQVR4nGNgGPKAkRhFygLm/5H5dz+chOtjItYm3TO9DEvf78MQJ2gAsu0dd88Tax9C8zrfW/+VBcxRMLIanGGgLGD+v9t2McPum9vhYrteLUPxP04DcGlGBjCDmJA1wTA+m9FdwILM6bZdzMDAwIDX2ehhgGIAOkDXjG47AwNSGKCbjEvDMAQAaWROSryIfg0AAAAASUVORK5CYII=
EOF
