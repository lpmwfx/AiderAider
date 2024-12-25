# KAider Simple Native

En simpel native Kotlin wrapper til Aider CLI værktøjet.

## Installation

1. Installer Aider:
```bash
pip install aider-chat
```

2. Installer Kotlin Native:
```bash
brew install kotlin-native
```

## Build

Kør build scriptet:
```bash
./build.sh
```

Dette vil generere en native eksekverbar fil kaldet `KAider.kexe`.

## Installation i PATH

For at gøre `kaider` tilgængelig globalt:
```bash
sudo cp KAider.kexe /usr/local/bin/kaider
```

## Brug

Efter installation kan du køre programmet direkte med:
```bash
kaider
```

## Features

- Ægte native LLVM-kompileret eksekverbar fil
- Minimal memory footprint
- Direkte system kald via posix
- Dansk sprog interface
- Terminal-only mode
- Chat historik i aider-chat.log
- Pæn formatering med farver
- Stream output
- Mørkt tema
- Forbedret input håndtering
- Viser prompts
- Debug information

## Afhængigheder

- Kotlin Native (LLVM baseret)
- Aider CLI tool
- Python 3.x 