import os

def rename_files(directory):
    for filename in os.listdir(directory):
        if filename.endswith(".png"):
            # Splitting the filename before the extension
            name, ext = os.path.splitext(filename)

            # Checking for the specific pattern "Tiny % Dust_0c.png" or similar
            if '_' in name and len(name.split('_')[-1]) == 2:
                new_name = name[:-1] + '_' + name[-1] + ext
                os.rename(os.path.join(directory, filename), os.path.join(directory, new_name))
                print(f"Renamed {filename} to {new_name}")

# Example usage
rename_files('C:\Users\colen\IdeaProjects\GT5-Unofficial\src\main\resources\assets\gregtech\textures\items\ModernMaterialsIcons\Metal_Shiny')

# Note: Uncomment the last line and replace "/path/to/directory" with the actual directory path to use the script.
