Postal Sorting Assistant – Android starter project (v0.2)
Developed by Mahesh Gupta

ఈ ZIP ఒక Android source project మాత్రమే; ఇది ఇంకా install చేసుకునే APK కాదు.

ప్రస్తుత ఫీచర్లు:
- Offline search
- Village / Hamlet, SO, BO, PIN ద్వారా search
- Same-name village duplicate alert
- SO / BO / PIN route details

డేటా పరిమితి:
- ప్రస్తుతం Classified List of SOs-BOs with hamlet villages.xlsx నుంచి 704 records మాత్రమే ఉన్నాయి.
- Consolidated.xlsx ఇంకా merge చేయలేదు.
- District / Mandal fields sourceలో స్పష్టంగా లేని చోట ఊహించి చేర్చలేదు.
- Official postal routingతో verify చేసిన తర్వాత మాత్రమే operational sortingకు ఉపయోగించాలి.

APK build:
ఈ projectకు Android Studioతో build చేయవచ్చు. కంప్యూటర్ లేని వారికి .github/workflows/build-apk.yml అనే GitHub Actions cloud-build workflow చేర్చాం. Source ZIPను GitHub repositoryలో upload చేసి Actions నుంచి Build Android APK workflow run చేయాలి. Build పూర్తయితే Actions runలో Artifacts నుంచి APK download చేయవచ్చు.

ఇంకా చేయాల్సినవి:
- Consolidated.xlsx import / reconciliation
- District / Mandal mapping verification
- Staff & Admin login
- Staff correction requests and Admin approval
- Excel import/update screen
- Multi-device sync and secure backend
- Release signing and real-device testing
