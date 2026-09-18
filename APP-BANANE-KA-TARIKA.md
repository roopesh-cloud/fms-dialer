# App banane ka poora tarika — step by step

Kuch install nahi karna. Sab browser me hi hoga. Kul ~20 minute, ek hi baar.

---

## Step 1 — GitHub ka free account (5 minute)

1. Browser me kholein: **https://github.com/signup**
2. Apna email (managingdirector@sanganericart.com), password, username daalein
3. Email par jo code aaye wo daal dein — account ban gaya

> Ye free hai. Card nahi maangta.

---

## Step 2 — Nayi jagah (repository) banayein (2 minute)

1. Upar-daayen **+** dabayein → **New repository**
2. **Repository name**: `fms-dialer`
3. **Private** chunein (zaroori — koi aur na dekhe)
4. Neeche **Create repository** dabayein

---

## Step 3 — Files upload karein (3 minute)

1. Jo page khula hai usme link dhoondein: **uploading an existing file**
   (ya seedha kholein: `https://github.com/<aapka-username>/fms-dialer/upload/main`)
2. Apne computer par `fms-dialer` folder kholein
   (tally-fms folder ke andar hai)
3. Us folder ke **andar ka saara saamaan** utha kar browser me **drag** kar dein
   — `app` folder, `.github` folder, aur `build.gradle` jaisi files.

   > DHYAN: `fms-dialer` naam ka folder mat daaliye — uske ANDAR jo hai wo daaliye.
   > Aur `.github` folder zaroor jana chahiye, wahi to app banata hai.

4. Neeche **Commit changes** dabayein

### Agar `.github` folder upload na ho paye

Kuch browser chhupe (dot se shuru hone wale) folder nahi bhejte. Tab
ye kar lijiye — 1 minute ka kaam:

1. Repository me **Add file** → **Create new file**
2. Naam wale khane me hubahu ye likhein:
   `.github/workflows/build.yml`
   (jaise hi `/` type karenge, folder apne aap ban jayega)
3. Apne computer par `fms-dialer/.github/workflows/build.yml` file
   Notepad me kholein, saara text copy karein, aur GitHub wale bade
   khane me paste kar dein
4. Neeche **Commit changes**

---

## Step 4 — App ban rahi hai (3 minute, apne aap)

1. Upar **Actions** tab dabayein
2. Ek line dikhegi peela gol ghoomta hua — matlab ban rahi hai
3. 2-3 minute me hara ✔ aa jayega

Agar laal ✘ aa jaye: us line par click karke jo likha aaye uska
screenshot bhej dein, main theek kar dunga.

---

## Step 5 — APK download (1 minute)

1. Us hare ✔ wali line par click karein
2. Page ke neeche **Artifacts** me: **SanganeriCart-FMS-apk**
3. Uspar click — zip download ho jayegi
4. Zip kholein → andar **app-debug.apk**

---

## Step 6 — Phone me install (har phone par 2 minute)

1. APK ko WhatsApp/email se CRM-SC ke phone par bhej dein
2. Phone me us file par tap → **Install**
3. "Unknown source" ka sawal aaye to **Allow / Settings → Allow from this source**
4. App khulegi → wahi login page → apna email-password daal kar login

### Do setting, ek hi baar (bahut zaroori)

**A. Call ki ijazat**
Pehli baar Call dabane par poochhega — **Allow** dabayein. Bas.
Agar galti se Deny ho gaya: Settings → Apps → SanganeriCart FMS →
Permissions → Phone → **Allow**

**B. Battery**
Settings → Apps → SanganeriCart FMS → Battery →
**Unrestricted** (Xiaomi/Vivo/Oppo/Realme me "No restrictions" /
"Don't optimise" likha hota hai)

---

## Ho gaya

Ab phone me **FMS** ka icon hai. Kholo → portal → party ke saamne hara
**Call** → **call lag gayi**. Ek tap.

WhatsApp wala **Msg** button bhi seedha WhatsApp khol dega.

---

## Baad me kuch badalna ho

Code me kuch badalna ho to GitHub par file edit kar ke Commit dabate hi
nayi APK apne aap ban jati hai — Actions me se download kar lena.
