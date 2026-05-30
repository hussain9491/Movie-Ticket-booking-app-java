---
name: Cinematic Noir
colors:
  surface: '#131313'
  surface-dim: '#131313'
  surface-bright: '#3a3939'
  surface-container-lowest: '#0e0e0e'
  surface-container-low: '#1c1b1b'
  surface-container: '#201f1f'
  surface-container-high: '#2a2a2a'
  surface-container-highest: '#353534'
  on-surface: '#e5e2e1'
  on-surface-variant: '#d2c5ac'
  inverse-surface: '#e5e2e1'
  inverse-on-surface: '#313030'
  outline: '#9b8f79'
  outline-variant: '#4f4633'
  surface-tint: '#f4bf19'
  primary: '#ffd15c'
  on-primary: '#3e2e00'
  primary-container: '#e8b400'
  on-primary-container: '#5f4800'
  inverse-primary: '#765a00'
  secondary: '#c6c4df'
  on-secondary: '#2f2e43'
  secondary-container: '#47475d'
  on-secondary-container: '#b8b6d0'
  tertiary: '#cbd6fc'
  on-tertiary: '#252f4d'
  tertiary-container: '#b0badf'
  on-tertiary-container: '#3f4a69'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#ffdf96'
  primary-fixed-dim: '#f4bf19'
  on-primary-fixed: '#251a00'
  on-primary-fixed-variant: '#594400'
  secondary-fixed: '#e2e0fc'
  secondary-fixed-dim: '#c6c4df'
  on-secondary-fixed: '#1a1a2e'
  on-secondary-fixed-variant: '#45455b'
  tertiary-fixed: '#dae1ff'
  tertiary-fixed-dim: '#bbc5eb'
  on-tertiary-fixed: '#0f1a37'
  on-tertiary-fixed-variant: '#3b4665'
  background: '#131313'
  on-background: '#e5e2e1'
  surface-variant: '#353534'
typography:
  headline-xl:
    fontFamily: Inter
    fontSize: 48px
    fontWeight: '800'
    lineHeight: 56px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.01em
  headline-lg-mobile:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 32px
  headline-md:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  body-lg:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.05em
  label-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 8px
  gutter: 24px
  margin-mobile: 16px
  margin-desktop: 48px
  container-max-width: 1280px
---

## Brand & Style
This design system is built to evoke the immersive, high-contrast atmosphere of a premium theater lobby. The aesthetic prioritizes depth and focus, utilizing a dark-mode-first approach that reduces visual noise and allows movie imagery to command attention.

The style draws from **Modern Corporate** principles with a **Cinematic** twist—blending the precision of professional desktop interfaces (JavaFX inspired) with the warmth of rich amber accents. The result is a UI that feels both authoritative and luxurious, catering to enthusiasts who value a frictionless yet atmospheric booking experience.

## Colors
The palette is anchored by a deep black background, creating a "lights-out" environment. Surfaces are rendered in dark navy and slate tones to provide structural hierarchy without breaking the immersion. 

The **Primary Accent (Amber)** is used sparingly for critical actions and active states, mimicking the glow of a projector or theater aisle lighting. Text remains high-contrast white for readability, while secondary information is pushed back using a muted slate-gray.

## Typography
The design system utilizes **Inter** exclusively to maintain a clean, technical, and professional feel. 

- **Headlines** use heavy weights (Bold/ExtraBold) with tight letter-spacing to create a sense of cinematic impact. 
- **Labels** often utilize semi-bold weights and slight tracking (letter-spacing) to ensure legibility against dark backgrounds. 
- **Body** copy is kept functional and balanced, ensuring that long descriptions remain comfortable to read in low-light environments.

## Layout & Spacing
The layout follows a **Fixed Grid** model for desktop to maintain the "pro-tool" feel of a JavaFX application, transitioning to a fluid model for mobile devices. 

- **Desktop:** 12-column grid with a 1280px max-width. Large 48px margins create a "stage" for the content.
- **Mobile:** 4-column grid with 16px gutters and margins, ensuring every pixel is utilized for content density.
- **Rhythm:** An 8px base unit governs all padding and margin decisions, ensuring a structured, predictable alignment across all views.

## Elevation & Depth
Depth is created through **Tonal Layering** rather than traditional shadows. Because the background is true black, shadows are often invisible. Instead, this design system uses:

1.  **Surface Tiers:** Background (#0D0D0D) sits at the bottom. Primary surfaces (#1A1A2E) sit above, and interactive elements/cards (#16213E) sit at the highest elevation.
2.  **Stroke Definition:** Thin, 1px borders in a slightly lighter slate are used to define edges of containers.
3.  **Amber Glow:** Interactive elements at the highest elevation utilize a subtle `0px 0px 12px rgba(232, 180, 0, 0.2)` outer glow when focused or active, mimicking an illuminated button.

## Shapes
The shape language is "Soft-Modern." All standard components like cards and input fields use a **0.5rem (8px)** corner radius. Larger layout containers and primary buttons use **1rem (16px)** to create a distinct visual hierarchy. This balance prevents the UI from feeling too sharp/aggressive while maintaining a professional, structured appearance.

## Components

### Buttons
- **Primary:** Pill-shaped (fully rounded), solid Amber background with black text.
- **Secondary:** Pill-shaped, transparent background with a 1px Amber border and Amber text.
- **States:** Hovering over primary buttons should increase the "Amber Glow" effect.

### Input Fields
- **Style:** Dark navy background (#16213E) with a subtle 1px border (#8892A4 at 20% opacity).
- **Focus State:** Border color changes to Amber with a subtle outer glow. Text is white, and labels are muted gray.

### Cards (Movie Posters/Info)
- **Structure:** Cards use the slate surface (#16213E). On hover, the border should brighten to Amber.
- **Content:** Posters should have a subtle 4px corner radius inside the 16px card container.

### Seat Selection (Specific to Product)
- **Available:** Dark slate outlines.
- **Selected:** Solid Amber.
- **Occupied:** Muted gray, 50% opacity.

### Data Tables
- **Style:** Clean, no vertical borders. Horizontal dividers use 1px slate.
- **Header:** Uppercase `label-md` typography with a darker background than the rows.