import os
import shutil
import random

# Destination
output_dir = "./test-apps"
os.makedirs(output_dir, exist_ok=True)

# Create subdirectories for nested scanning
subdirs = ["set1", "set2", "set3", "set4", "set5"]
for sub in subdirs:
    os.makedirs(os.path.join(output_dir, sub), exist_ok=True)

# Category keywords
category_keywords = {
    "Browser": ["chrome", "firefox", "safari", "opera"],
    "Media": ["vlc", "spotify", "media", "music"],
    "IDE": ["vscode", "intellij", "eclipse", "code"],
    "Office": ["word", "excel", "powerpoint", "office"],
    "Games": ["game", "steam", "minecraft"],
    "System": ["driver", "update", "antivirus"],
    "Development": ["java", "python", "compiler", "maven"],
    "Communication": ["skype", "discord", "zoom"]
}

valid_extensions = [".exe", ".msi", ".apk", ".jar"]

# Content for duplicate files
duplicate_content = b"This is duplicate content used in multiple files.\n" * 10

# Create 30 duplicate files in nested directories with varying sizes and extensions

# Add 10 intentionally detectable duplicates (same content)
known_duplicate_content = b"DETECTABLE_DUPLICATE_CONTENT_BLOCK\n" * 1000  # ~30KB
for i in range(10):
    ext = random.choice(valid_extensions)
    subdir = random.choice(subdirs)
    name = f"known_duplicate_{i}{ext}"
    with open(os.path.join(output_dir, subdir, name), "wb") as f:
        f.write(known_duplicate_content)

for i in range(30):
    ext = random.choice(valid_extensions + [e.upper() for e in valid_extensions])
    subdir = random.choice(subdirs)
    name = f"duplicate_{i}{ext}"
    content_multiplier = random.randint(50, 5000)  # Between ~1KB and ~5MB
    content = duplicate_content * content_multiplier
    with open(os.path.join(output_dir, subdir, name), "wb") as f:
        f.write(content)

# Create 50 unique files with occasional duplicates across categories and subdirs
unique_tracker = {}
for category, keywords in category_keywords.items():
    for i, keyword in enumerate(keywords):
        ext = random.choice(valid_extensions + [e.upper() for e in valid_extensions])
        subdir = random.choice(subdirs)
        name = f"{keyword}_tool_{i}{ext}"

        # Occasionally reuse content to simulate edge-case duplicates
        if keyword in unique_tracker:
            content = unique_tracker[keyword]
        else:
            content = (f"Unique content for {category} - {keyword}\n".encode() *
                       random.randint(100, 3000))  # ~1KB to 3MB
            if random.random() < 0.3:
                unique_tracker[keyword] = content

        with open(os.path.join(output_dir, subdir, name), "wb") as f:
            f.write(content)

# Add 40 random but categorizable files
for _ in range(40):
    category = random.choice(list(category_keywords.keys()))
    keyword = random.choice(category_keywords[category])
    ext = random.choice(valid_extensions + [e.upper() for e in valid_extensions])
    subdir = random.choice(subdirs)
    noise = ''.join(random.choices("xyz123", k=5))
    name = f"{noise}_{keyword}_app{ext}"
    content = (f"App related to {category} using keyword {keyword}\n".encode() *
               random.randint(50, 500))  # ~1KB to 50KB
    with open(os.path.join(output_dir, subdir, name), "wb") as f:
        f.write(content)


# Add 20 misclassified or ambiguous files
for _ in range(20):
    category1 = random.choice(list(category_keywords.keys()))
    category2 = random.choice(list(category_keywords.keys()))
    keyword1 = random.choice(category_keywords[category1])
    keyword2 = random.choice(category_keywords[category2])
    ext = random.choice(valid_extensions + [e.upper() for e in valid_extensions])
    subdir = random.choice(subdirs)
    noise = ''.join(random.choices("abcXYZ789", k=4))
    name = f"{noise}_{keyword1}_{keyword2}_ambiguous{ext}"
    content = (f"File with mixed relevance: {keyword1}, {keyword2}\n".encode() *
               random.randint(50, 300))  # ~1KB to 30KB
    with open(os.path.join(output_dir, subdir, name), "wb") as f:
        f.write(content)

print(f"✅ Generated test files with nested folders and variable content in: {output_dir}")