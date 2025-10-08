#!/usr/bin/env python3
"""
Adaptive Ores Texture Generator

This script generates overlay textures for the Adaptive Ores mod.
Each overlay is a transparent PNG with ore-specific patterns that will be
rendered on top of dynamic backdrop materials.
"""

import os
import sys
from PIL import Image, ImageDraw, ImageFilter
import random
import math

# Ore definitions with colors and patterns
ORE_DEFINITIONS = {
    'coal': {
        'color': (32, 32, 32),          # Dark gray/black
        'accent': (64, 64, 64),         # Lighter gray
        'pattern': 'scattered_spots',
        'density': 0.15
    },
    'iron': {
        'color': (139, 125, 95),        # Brownish
        'accent': (180, 160, 120),      # Lighter brown
        'pattern': 'veins',
        'density': 0.12
    },
    'copper': {
        'color': (184, 115, 51),        # Copper orange
        'accent': (220, 140, 70),       # Bright copper
        'pattern': 'scattered_spots',
        'density': 0.14
    },
    'gold': {
        'color': (255, 215, 0),         # Gold yellow
        'accent': (255, 245, 100),      # Bright gold
        'pattern': 'veins',
        'density': 0.10
    },
    'redstone': {
        'color': (220, 0, 0),           # Bright red
        'accent': (255, 100, 100),      # Light red
        'pattern': 'crystalline',
        'density': 0.16
    },
    'lapis': {
        'color': (31, 81, 153),         # Lapis blue
        'accent': (70, 120, 200),       # Lighter blue
        'pattern': 'crystalline',
        'density': 0.13
    },
    'diamond': {
        'color': (185, 242, 255),       # Light cyan
        'accent': (225, 255, 255),      # White-cyan
        'pattern': 'crystalline',
        'density': 0.08
    },
    'emerald': {
        'color': (80, 220, 80),         # Bright green
        'accent': (120, 255, 120),      # Light green
        'pattern': 'crystalline',
        'density': 0.09
    }
}

def create_scattered_spots_pattern(image, draw, color, accent, density, size=16):
    """Create scattered circular spots pattern"""
    num_spots = int(size * size * density)
    
    for _ in range(num_spots):
        x = random.randint(0, size - 1)
        y = random.randint(0, size - 1)
        
        # Random spot size (1-3 pixels)
        spot_size = random.randint(1, 3)
        
        # Use main color or accent randomly
        spot_color = color if random.random() < 0.7 else accent
        
        if spot_size == 1:
            draw.point((x, y), fill=spot_color)
        else:
            draw.ellipse([x-spot_size//2, y-spot_size//2, 
                         x+spot_size//2, y+spot_size//2], fill=spot_color)

def create_veins_pattern(image, draw, color, accent, density, size=16):
    """Create vein-like patterns"""
    num_veins = int(size * density * 2)
    
    for _ in range(num_veins):
        # Random starting point
        start_x = random.randint(0, size - 1)
        start_y = random.randint(0, size - 1)
        
        # Random direction and length
        angle = random.uniform(0, 2 * math.pi)
        length = random.randint(3, 8)
        
        # Draw vein
        vein_color = color if random.random() < 0.8 else accent
        
        for i in range(length):
            x = int(start_x + i * math.cos(angle))
            y = int(start_y + i * math.sin(angle))
            
            if 0 <= x < size and 0 <= y < size:
                draw.point((x, y), fill=vein_color)
                
                # Add some thickness randomly
                if random.random() < 0.3:
                    for dx, dy in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
                        nx, ny = x + dx, y + dy
                        if 0 <= nx < size and 0 <= ny < size:
                            draw.point((nx, ny), fill=vein_color)

def create_crystalline_pattern(image, draw, color, accent, density, size=16):
    """Create crystalline/angular patterns"""
    num_crystals = int(size * density * 1.5)
    
    for _ in range(num_crystals):
        # Random crystal center
        cx = random.randint(1, size - 2)
        cy = random.randint(1, size - 2)
        
        # Crystal size
        crystal_size = random.randint(1, 3)
        crystal_color = color if random.random() < 0.6 else accent
        
        # Draw angular shapes
        if crystal_size == 1:
            draw.point((cx, cy), fill=crystal_color)
        else:
            # Draw small angular shapes
            points = []
            num_points = random.randint(3, 6)
            for _ in range(num_points):
                angle = random.uniform(0, 2 * math.pi)
                radius = random.uniform(0.5, crystal_size)
                x = int(cx + radius * math.cos(angle))
                y = int(cy + radius * math.sin(angle))
                x = max(0, min(size - 1, x))
                y = max(0, min(size - 1, y))
                points.append((x, y))
            
            if len(points) >= 3:
                try:
                    draw.polygon(points, fill=crystal_color)
                except:
                    # Fallback to points if polygon fails
                    for point in points:
                        draw.point(point, fill=crystal_color)

def generate_overlay_texture(ore_type, size=16, output_path=None):
    """Generate an overlay texture for the specified ore type"""
    if ore_type not in ORE_DEFINITIONS:
        raise ValueError(f"Unknown ore type: {ore_type}")
    
    config = ORE_DEFINITIONS[ore_type]
    
    # Create transparent image
    image = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(image)
    
    # Set random seed for reproducible textures
    random.seed(ord(ore_type[0]) * 12345)
    
    # Generate pattern based on ore type
    if config['pattern'] == 'scattered_spots':
        create_scattered_spots_pattern(image, draw, config['color'], config['accent'], config['density'], size)
    elif config['pattern'] == 'veins':
        create_veins_pattern(image, draw, config['color'], config['accent'], config['density'], size)
    elif config['pattern'] == 'crystalline':
        create_crystalline_pattern(image, draw, config['color'], config['accent'], config['density'], size)
    
    # Apply slight blur to soften the edges. We want to allow semi-transparent
    # pixels (for smooth edges), but transparent pixels with RGB=(0,0,0)
    # cause black fringes when blended. To avoid that, we'll keep the alpha
    # (including semi-transparent values) but "bleed" nearby opaque colors
    # into fully transparent pixels (alpha == 0) so sampling doesn't pick
    # black RGB values.
    image = image.filter(ImageFilter.GaussianBlur(radius=0.6))

    # Bleed neighbor colors into fully-transparent pixels to avoid black fringe
    # when rendering with alpha blending. For each fully transparent pixel,
    # average the RGB of neighboring pixels that have alpha > 0 and assign
    # that RGB to the transparent pixel while keeping alpha at 0.
    pixels = image.load()
    width, height = image.size

    # Copy original RGBA data to avoid reading modified pixels during pass
    orig = [pixels[x, y] for y in range(height) for x in range(width)]

    def get_orig(x, y):
        if x < 0 or x >= width or y < 0 or y >= height:
            return (0, 0, 0, 0)
        return orig[y * width + x]

    for y in range(height):
        for x in range(width):
            r, g, b, a = get_orig(x, y)
            if a == 0:
                # gather neighbor colors with alpha > 0
                nr = ng = nb = count = 0
                for oy in range(y - 1, y + 2):
                    for ox in range(x - 1, x + 2):
                        if ox == x and oy == y:
                            continue
                        cr, cg, cb, ca = get_orig(ox, oy)
                        if ca > 0:
                            nr += cr
                            ng += cg
                            nb += cb
                            count += 1
                if count > 0:
                    nr //= count
                    ng //= count
                    nb //= count
                    # set RGB to average but keep alpha=0
                    pixels[x, y] = (nr, ng, nb, 0)
                else:
                    # fallback leave as black transparent
                    pixels[x, y] = (0, 0, 0, 0)
    
    if output_path:
        image.save(output_path, 'PNG')
        print(f"Generated {ore_type} overlay texture: {output_path}")
    
    return image

def generate_base_ore_texture(ore_type, size=16, output_path=None):
    """Generate a base texture (stone with ore overlay) for fallback display"""
    if ore_type not in ORE_DEFINITIONS:
        raise ValueError(f"Unknown ore type: {ore_type}")
    
    # Create stone base (gray texture)
    stone_color = (125, 125, 125)
    stone_variant = (100, 100, 100)
    
    image = Image.new('RGBA', (size, size), stone_color)
    draw = ImageDraw.Draw(image)
    
    # Add stone texture variation
    random.seed(42)  # Consistent stone pattern
    for _ in range(size * size // 4):
        x = random.randint(0, size - 1)
        y = random.randint(0, size - 1)
        if random.random() < 0.3:
            draw.point((x, y), fill=stone_variant)
    
    # Get overlay
    overlay = generate_overlay_texture(ore_type, size)
    
    # Composite overlay onto stone
    image = Image.alpha_composite(image.convert('RGBA'), overlay)
    
    if output_path:
        image.save(output_path, 'PNG')
        print(f"Generated {ore_type} base texture: {output_path}")
    
    return image

def main():
    """Main function to generate all textures"""
    # Get script directory and mod directory
    script_dir = os.path.dirname(os.path.abspath(__file__))
    mod_dir = script_dir  # Script is in mod root directory
    
    # Texture directories
    textures_dir = os.path.join(mod_dir, 'src', 'main', 'resources', 'assets', 'adaptiveores', 'textures', 'block')
    overlay_dir = os.path.join(textures_dir, 'overlay')
    
    # Create directories if they don't exist
    os.makedirs(textures_dir, exist_ok=True)
    os.makedirs(overlay_dir, exist_ok=True)
    
    print("Generating Adaptive Ores Textures...")
    print(f"Output directory: {textures_dir}")
    print(f"Overlay directory: {overlay_dir}")
    
    # Generate textures for each ore type
    for ore_type in ORE_DEFINITIONS.keys():
        # Generate overlay texture
        overlay_path = os.path.join(overlay_dir, f'{ore_type}_ore_overlay.png')
        generate_overlay_texture(ore_type, 16, overlay_path)
        
        # Generate base texture (for fallback display)
        base_path = os.path.join(textures_dir, f'adaptive_{ore_type}_ore.png')
        generate_base_ore_texture(ore_type, 16, base_path)
    
    print("\nTexture generation complete!")
    print("\nGenerated files:")
    
    # List generated files
    for ore_type in ORE_DEFINITIONS.keys():
        overlay_file = f'{ore_type}_ore_overlay.png'
        base_file = f'adaptive_{ore_type}_ore.png'
        print(f"  - {overlay_file}")
        print(f"  - {base_file}")

if __name__ == "__main__":
    try:
        main()
    except ImportError as e:
        print("Error: Missing required dependency. Install PIL/Pillow with:")
        print("  pip install Pillow")
        print(f"Error details: {e}")
        sys.exit(1)
    except Exception as e:
        print(f"Error generating textures: {e}")
        sys.exit(1)