# 🔥 Monster Golem Mod - Minecraft 1.20.1 Forge

**Ege'nin Süper Koruyucu Golemi** - Minecraft dünyasında muazzam güçleriyle sizin yanınızda savaşan efsanevi bir golem!

## 📋 İçindekiler
- [Özellikler](#-özellikler)
- [Kurulum](#-kurulum)
- [Başlangıç](#-başlangıç)
- [Monster Golem Özellikleri](#-monster-golem-özellikleri)
- [İstatistikler](#-oyun-içi-istatistikler)
- [Yetenekler ve Saldırılar](#-yetenekler-ve-saldırılar)
- [Armor Sistemi](#-armor-sistemi)
- [Leveling Sistemi](#-leveling-sistemi)
- [Hedefler](#-hedefler)
- [İpuçları ve Tricks](#-ipuçları-ve-tricks)

## ✨ Özellikler

✅ **Güçlü Savaş Yardımcısı** - 300 HP ile başlayan devasa golem  
✅ **Akıllı AI Sistemi** - Rakiplerini otomatik olarak algılar ve hedefler  
✅ **Ranged Saldırılar** - Ateş topu ve kartopu atışı  
✅ **Boss Bar** - Goleminin sağlığını ekranda görebilirsiniz  
✅ **Armor Destekleri** - Çeşitli zırh seviyeleriyle güçlendirin  
✅ **Leveling Sistemi** - Güçlenme ve gelişme  
✅ **Creative Tab** - Oyun içinde kolay erişim  

---

## 🚀 Kurulum

### Gereksinimler
- **Minecraft 1.20.1**
- **Forge 47.4.16 veya üzeri**
- **Java 17 veya üzeri**

### Kurulum Adımları

#### 1. **Mod Dosyasını İndir ve Yerleştir**
1. Bu mod projesini build ettikten sonra JAR dosyasını oluştur:
   ```bash
   ./gradlew build
   ```
   
2. Build edilen JAR dosyasını bul:
   ```
   build/libs/customgolem-[version].jar
   ```

3. Dosyayı Minecraft mods klasörüne kopyala:
   - **Windows**: `%appdata%\.minecraft\mods\`
   - **Linux**: `~/.minecraft/mods/`
   - **macOS**: `~/Library/Application Support/minecraft/mods/`

#### 2. **Minecraft Launcher Ayarları**
1. Minecraft Launcher'ı aç
2. Sol tarafta "Installations" bölümüne git
3. Forge 1.20.1 kurulu bir profil seç veya oluştur
4. "Play" butonuna tıkla

#### 3. **Kurulumu Doğrula**
- Oyuna girdikten sonra mods menüsüne bak
- "Monster Golem Mod" veya "customgolem" modulü görmeniz gerekir

---

## 🎮 Başlangıç

### Monster Golem Oluşturma

1. **Spawn Egg ile (En Kolay)**
   - Creative modda Creative Inventory'den "Monster Golem Spawn Egg" bul (siyah-kırmızı renk)
   - Dünya üzerine koy
   - Monster Golem ortaya çıkacak!

2. **Survival Modda**
   - Commands kullan: `/summon customgolem:monster_golem`
   - Veya spawn eggini obtain etmek için mods yükle (creative gibi)

---

## 🦾 Monster Golem Özellikleri

### 🎯 Görünüş
- **Model**: Customize edilmiş Iron Golem varyantı
- **İsim**: "Ege'nin Süper Koruyucu Golemi [Lv.X]"
- **Boss Bar**: Yeşil renkli health bar

### 📊 Oyun İçi İstatistikler

| İstatistik | Değer | Açıklama |
|---|---|---|
| **Başlangıç HP** | 300 ❤️ | Normal Iron Golem'den 10x daha fazla |
| **Saldırı Hasarı** | 35 ⚔️ | Çok yüksek hasara sahip melee saldırı |
| **Hareket Hızı** | 0.30 🏃 | Biraz yavaş ama çok güçlü |
| **Knockback Direnci** | 1.0 (Max) 💪 | Hiç geri itilmez |
| **Algılama Menzili** | 48 blok 👁️ | Çok uzaktan düşmanları görür |

### 👊 Yetenekler ve Saldırılar

#### 1. **Melee Saldırı (Yakın Dövüş)**
- **Hasar**: 35 puan (base)
- **Creeper'e Özel**: 2.5x hasar (87.5 puan!)
- Her vurdu mu düşman geri itilir

#### 2. **Fireball Saldırısı 🔥**
- **Menzil**: 20 blok
- **Cooldown**: 4 saniye (80 ticks)
- **Hasar**: Alansal hasara + yangın etkisi
- Düşmanları grup modunda öldürmek için perfect

#### 3. **Snowball Saldırısı ❄️**
- **Menzil**: 15 blok
- **Cooldown**: 2 saniye (40 ticks)
- **Hasar**: Hafif hasara (snowball halinde)
- Düşmanları yavaşlatır

---

## 🛡️ Armor Sistemi

Golemini çeşitli armor maddeleriyle güçlendir!

### Armor Türleri ve Bonusları

| Armor Türü | Bonus | Açıklama |
|---|---|---|
| **Deri** | +2 🧥 | Temel koruma |
| **Zincir** | +4 ⛓️ | Orta koruma |
| **Demir** | +6 🛡️ | Iyi koruma |
| **Altın** | +5 👑 | Süslü ama daha az etkili |
| **Elmas** | +10 💎 | Güçlü koruma |
| **Netherite** | +15 🔥 | Maksimum koruma |

### Armor Kullanımı
1. Istediğiniz armor parçasını alın
2. Golemle sağ tıkla
3. Armor eklenir ve hasar azalır

---

## ⬆️ Leveling Sistemi

Monster Golem her düşmanı öldürdüğünde XP kazanır ve güçlenecek!

### Level Atlama Mekanizması
- **Başlangıç XP Gereksinimi**: `level × 20`
- **Örnek**: 
  - Level 1 → 2: 20 XP
  - Level 2 → 3: 40 XP
  - Level 10 → 11: 200 XP

### Level Atladığında Bonuslar
- ⚔️ **Saldırı Hasarı**: +3 puan
- ❤️ **Max HP**: +20 (+ 20 iyileştirme)

### Level Takibi
- Goleminin seviyesi, adında gösterilir: "Ege'nin Süper Koruyucu Golemi [Lv.50]"
- Boss bar'da gerçek zamanlı health gösterilir

---

## 🎯 Hedefler (Düşmanlar)

Monster Golem şu varlıklara otomatik olarak saldırır:

| Hedef | Menzil | Açıklama |
|---|---|---|
| **Tüm Monsterler** | 48 blok 👹 | Zombie, Skeleton, Witch, Spider vb. |
| **Slime/MagmaCube** | 48 blok 🧿 | Yumuşak yaratıklar |
| **Creeper** | 64 blok 🟢 | Özel olarak 64 bloktan görülür |
| **Node Açılmamış** | - | Oyuncuya saldırmaz, müttefiktir |

### Menzil Avantajı
- Creeper'leri 64 bloktan görür (çok uzaktan bile)
- 48 blok genel algılama menzili muazzam
- Yıkıcı creeper patlamalarından kurtulun!

---

## 💡 İpuçları ve Tricks

### 🏹 Optimal Kullanım
1. **Değerli Biome'larda Koruma**: Netherite veya Elmas Armor ver
2. **Grup Kontrollü**: Ateş topu ve kartopu ile grup düşmanları kontrol et
3. **Şehir Savunması**: Başlangıç noktanda bekletip etraf savunması yap
4. **XP Farması**: Yaşlı seviye için düşman spawn'larının yanına koy

### 🎮 Gelişmiş Taktikler
- **Creeper Fokuslanması**: 2.5x hasar ile creeper'ler anında öldürülür
- **Knockback Rezistansı**: Boss'larla savaşırken hiç geri itilmez
- **Ranged Kombinasyon**: Fireball + Snowball kombinasyonu group control için harika
- **Boss Bar Takip**: Boss bar ile sağlığını her zaman gözle

### 🪛 Level Çiftlikçiliği (Grinding)
```
1. Mob spawner yakınlarına golem koy
2. Düşmanlar otomatik olarak hedeflenir
3. Her öldürülen düşman = XP kazancı
4. Level 50+ golem çok güçlü olur
```

---

## ⚙️ Teknik Detaylar

### Mod ID
```
customgolem
```

### Sınıflar
- `MonsterGolemEntity` - Ana golem entity sınıfı
- `MonsterGolemRenderer` - 3D render ve görünüm
- `GolemArmorItemsLayer` - Armor display sistemi

### Dosya Yapısı
```
src/main/java/com/kadirergun/monstergolem/
├── MonsterGolemMod.java (Ana mod sınıfı)
├── ModEntities.java (Entity kaydı)
├── ModItems.java (Spawn egg kaydı)
├── entity/
│   └── MonsterGolemEntity.java (Core logic)
└── client/
    ├── MonsterGolemRenderer.java (Render)
    └── GolemArmorItemsLayer.java (Armor layer)
```

### Konfigürasyon (Gelecek Sürüm)
`config/customgolem-config.toml` dosyasında özelleştirilebilir:
- Başlangıç HP
- Saldırı Hasarı
- Cooldown Süreleri
- Level Bonusları

---

## 🐛 Sorun Giderme

### Mod Yüklenmiyor
❌ **Sorun**: Mod listesinde görülmüyor
✅ **Çözüm**: 
- Forge yüklü mü kontrol et
- Mod dosyasının `mods/` klasöründe olduğundan emin ol
- Minecraft'ı yeniden başlat

### Golem Oluşturulamıyor
❌ **Sorun**: Spawn egg çalışmıyor
✅ **Çözüm**:
- Creative modda olduğundan emin ol
- Command kullan: `/summon customgolem:monster_golem`
- Eğer entity kaydedilmediyse mod kötü yüklendi

### Golem Hiç Saldırmıyor
❌ **Sorun**: Golem düşmanları görmüyor/saldırmıyor
✅ **Çözüm**:
- Golem spawn wave'de düşman var mı kontrol et
- Spawn location'ı açık alan yap (çok yakın bloklar)
- Server de mod yüklü mü kontrol et

---

## 📝 Version Tarihi

### v1.0.0
- ✨ İlk sürüm yayınlandı
- 🔥 Fireball ve Snowball yetenekleri
- 🛡️ Armor sistemi
- ⬆️ Leveling sistemi
- 👁️ Boss bar ekranı

---

## 🎨 Çevre İlişkisi

### Biome Uyumu
Monster Golem tüm biome'larda güzel çalışır:
- 🏔️ Dağlar - Yüksek yerlerde harika savunma
- 🌲 Ormanlar - Creeper kontrollü için ideal
- 🏜️ Çöl - Açık alanda maksimum etkinlik
- 🌊 Su / Kumul - Su kaçınıyor, hızlı hareket edemez

---

## 📜 Lisans

Bu mod Minecraft Forge Modding Lisansı altında yayımlanmıştır.
Ticari kullanım yok, özgür şekilde kullan ve dağıt.

---

## 👨‍💻 Geliştirici

**Kadir Ergun** - Monster Golem Mod Yaratıcısı

### Planlanmış Özellikler (Roadmap)
- 🎮 Golem Customization Komutları
- ⚡ Yeni Yetenekler (Lightning Strike, Sonic Boom)
- 🧬 Genetik Sistem (Parent Golem → Baby Golem)
- 🌍 Dünya Koruma Sistemi
- 📊 İstatistik Takip Sistemi
- 🎨 Görünüş Özelleştirme

---

## 🙏 Teşekkürler

- Minecraft Forge Topluluğu
- MCP (Mod Coder Pack)
- Tüm test yapan oyuncular

---

## ❓ Sorular ve Destek

Sorularınız veya sorunlar için:
- Yorumlar bölümünde paylaş
- GitHub Issues'de bildir
- Geliştirici ile iletişime geç

**Son Güncelleme**: Minecraft 1.20.1 Forge 47.4.16 (Şubat 2026)

---

🎉 **Hayır Eğlenceli Oyunlar! Monster Golem ile Minecraft'ı Fethettir!** 🎉
