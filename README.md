# Bridge Android Technical Test

## Overview
This Android application is a student management system that allows users to view, create, edit, and delete pupil records. 
The app demonstrates modern Android development practices using Jetpack Compose, MVVM architecture, and clean code principles.

## Features

### Pupil Management
- **View Pupils**: Browse a list of pupils with pagination support
- **Pupil Details**: View detailed information about each pupil
- **Create Pupils**: Add new pupils with name, country, location coordinates, and image URL
- **Edit Pupils**: Modify existing pupil information
- **Delete Pupils**: Remove pupils with confirmation dialog and error handling
- **Pull-to-Refresh**: Reload pupil data from the server

### Technical Features
- **Offline Support**: Local database storage for offline access and creation
- **Error Handling**: Graceful error handling with user feedback
- **Loading States**: Visual indicators during network operations

## Architecture

The application follows the MVVM (Model-View-ViewModel) architecture pattern with Clean Architecture principles:

### Layers
- **Presentation Layer**: Jetpack Compose UI components and ViewModels
- **Domain Layer**: Repository interfaces defining the business logic
- **Data Layer**: Repository implementations, API services, and local database

## API Integration

The app connects to a RESTful API at `androidtechnicaltestapi-test.bridgeinternationalacademies.com` with the following endpoints:

- `GET /pupils?page={page}`: Retrieve paginated list of pupils
- `GET /pupils/{id}`: Get details for a specific pupil
- `POST /pupils`: Create a new pupil
- `PUT /pupils/{id}`: Update an existing pupil
- `DELETE /pupils/{id}`: Delete a pupil

The app handles API failures gracefully by falling back to local database operations when necessary.