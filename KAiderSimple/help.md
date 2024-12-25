# Aider Chat Kommandoer

## Fil Håndtering
- `/add <filnavn>` - Tilføj en fil til chatten
- `/drop <filnavn>` - Fjern en fil fra chatten
- `/ls` - List filer i arbejdsmappen
- `/cat <filnavn>` - Vis indholdet af en fil

## Navigation og Søgning
- `@<filnavn>` - Søg efter og referer til en fil
- `/find <søgeterm>` - Søg i kodebasen
- `/grep <pattern>` - Søg efter specifikt mønster
- `/where` - Vis nuværende arbejdsmappe

## Git Kommandoer
- `/commit` - Commit ændringer
- `/diff` - Vis ændringer
- `/status` - Git status
- `/undo` - Fortryd sidste ændring

## Chat Kontrol
- `/clear` - Ryd chat historik
- `/exit` - Afslut Aider
- `/help` - Vis denne hjælp
- `/history` - Vis chat historik
- `/tokens` - Vis token forbrug
- `/voice` - Start stemmeinput (hvis aktiveret)

## Editor Kommandoer
- `/edit <filnavn>` - Åbn fil i editor
- `/run <kommando>` - Kør shell kommando
- `/lint` - Kør linter
- `/test` - Kør tests

## Avancerede Features
- `/map` - Vis repository map
- `/prompts` - Vis aktive prompts
- `/settings` - Vis aktuelle indstillinger
- `/version` - Vis Aider version

## Tips
- Brug TAB for auto-completion af filnavne efter @
- Brug pile-taster for at navigere i kommando historik
- Brug CTRL+C to gange for at afslutte
- Alle kommandoer kan forkortes (fx `/a` for `/add`)

## Eksempler
```bash
/add KAider.main.kts    # Tilføj hovedfil
@KAi<TAB>              # Auto-complete til @KAider.main.kts
/ls                    # List filer
/find function         # Søg efter "function"
```

## Konfiguration
Scriptet `KAider.main.kts` er sat op med:
- Dansk sprog
- Mørkt tema
- Git integration
- Fil overvågning
- Chat historik i aider-chat.log 