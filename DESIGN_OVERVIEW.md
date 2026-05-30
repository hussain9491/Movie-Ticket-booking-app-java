# CineBook Design Overview

This project is being aligned to the Google Stitch UI/UX reference folder in `stitch_cinebook_javafx_management_system`.

## Reference folder mapping

- `cinebook_login/` -> `src/main/resources/com/example/cinebook/view/login.fxml`
- `cinebook_home/` -> `src/main/resources/com/example/cinebook/view/home.fxml`
- `cinebook_seat_selection/` -> `src/main/resources/com/example/cinebook/view/seat.fxml`
- `cinebook_booking_confirmation/` -> `src/main/resources/com/example/cinebook/view/confirm.fxml`
- `cinebook_my_bookings/` -> `src/main/resources/com/example/cinebook/view/bookings.fxml`
- `cinebook_admin_panel/` -> `src/main/resources/com/example/cinebook/view/admin.fxml`
- `cinematic_noir/DESIGN.md` -> shared theme tokens in `src/main/resources/com/example/cinebook/css/dark-cinema.css`

## Visual language used in the app

- Background: deep near-black surfaces (`#131313`, `#0D0D0D`)
- Primary accent: amber/gold (`#FFD15C`, `#E8B400`)
- Surface hierarchy: layered cards, panels, and tables
- Typography: Inter-style headline/label/body system
- Controls: pill buttons, rounded inputs, glow on primary actions
- Tables: dark rows, muted headers, amber selection state
- Seat states: available, VIP, booked, selected

## Screen-by-screen summary

### Login
- Split hero + form layout
- Cinematic mood background
- Elevated login card with primary CTA and secondary register action

### Home / Movies
- Dashboard top bar with search and filters
- Movie grid cards with dark surfaces and amber hover accent
- Fits the immersive “now showing” overview from the reference

### Seat Selection
- Theatre screen header at top
- 8x12 seat grid powered by `SeatController`
- Order summary panel and legend aligned with the seat-selection reference

### Booking Confirmation
- Summary card with movie, showtime, seats, and total amount
- Payment reference input and confirm/back actions

### My Bookings
- Bookings toolbar and full-width table
- Clear status and action styling for confirmed/cancelled bookings

### Admin Panel
- Stat cards for movies, users, bookings, and revenue
- Add movie form and management tables
- Matches the admin dashboard section of the Stitch reference set

## Implementation notes

- Existing controller bindings were preserved.
- The shared theme lives in `dark-cinema.css` and can be tuned further if you want a pixel-perfect pass.
- The project is currently verified with Maven tests after the UI refresh.

