# SanganeriCart FMS — team ke phone wali app

Ye wahi portal hai jo browser me chalta hai. Farak sirf ek:

> Portal me hara **Call** button dabate hi **call lag jati hai** —
> dialer screen beech me nahi aati, green button dabana nahi padta.

Call company ke apne SIM se hi jati hai. Customer ko wahi number dikhta hai.

Browser ye kabhi nahi kar sakta — Android call lagane ki ijazat (`CALL_PHONE`)
sirf app ko deta hai. Isi wajah se TeleCRM jaise sabhi CRM ki apni app hoti hai.

---

## APK kaise banegi (computer par kuch install nahi karna)

1. GitHub par is folder ko upload kar dein
2. Upar **Actions** tab kholein
3. Build apne aap chalegi (2-3 minute)
4. Build khulein → neeche **SanganeriCart-FMS-apk** → download
5. Zip kholein → `app-debug.apk` → phone me daal kar install

Poora tarika screenshot ke saath: `APP-BANANE-KA-TARIKA.md`

## Phone me install ke baad

- Pehli call par ek baar **"Allow phone calls"** poochhega — Allow dabayein.
  Bas ek hi baar.
- App ki battery setting **"Unrestricted / Don't optimise"** kar dein,
  taaki background me band na ho.

## Link kabhi badle to

`MainActivity.java` me sirf ek line:

```java
private static final String DEFAULT_URL = "https://sanganeri-fms.pages.dev/";
```

Ya app me hi — net na ho to jo screen aati hai uspar **Change link** ka
button hai.
