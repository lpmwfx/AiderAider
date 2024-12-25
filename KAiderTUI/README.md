# KAider TUI

En simpel terminal brugerflade til KAider uden eksterne dependencies.

## Features

- Tre-kolonne layout:
  - Files (filliste)
  - Chat (Aider interaktion)
  - Preview (fil preview)
- Farver via ANSI escape codes
- Unicode symboler
- Automatisk window sizing
- Ren Kotlin implementation

## Installation

Ingen ekstra installation nødvendig! Kør bare:

```bash
chmod +x KAider.kts
./KAider.kts
```

## Keyboard Shortcuts

- `q` - Afslut
- (Flere keyboard shortcuts kommer snart)

## Implementation

- Bruger kun standard Kotlin
- ANSI escape codes til farver og cursor kontrol
- `tput` til terminal dimensioner
- Ingen eksterne dependencies 