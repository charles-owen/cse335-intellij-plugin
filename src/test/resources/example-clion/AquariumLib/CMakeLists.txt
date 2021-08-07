project(AquariumLib VERSION 0.01)

set(SOURCE_FILES
        pch.h
        AquariumApp.cpp AquariumApp.h
        MainFrame.cpp MainFrame.h AquariumApp.cpp AquariumApp.h AquariumView.cpp AquariumView.h Aquarium.cpp Aquarium.h Item.cpp Item.h FishBeta.cpp FishBeta.h ids.h FishAngel.cpp FishAngel.h FishNemo.cpp FishNemo.h FishCarp.cpp FishCarp.h)

add_library(${PROJECT_NAME} STATIC ${SOURCE_FILES})

target_precompile_headers(${PROJECT_NAME} PUBLIC pch.h)