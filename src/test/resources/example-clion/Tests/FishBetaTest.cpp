#include "gtest/gtest.h"
#include <pch.h>
#include <Aquarium.h>
#include <FishBeta.h>

TEST(FishBetaTest, Construct) {
    Aquarium aquarium;
    FishBeta fish(&aquarium);
}

TEST(FishBetaTest, HitTest) {
    // Create a fish to test
    Aquarium aquarium;
    FishBeta fish(&aquarium);

    // Give it a location
    // Always make the numbers different, in case they are mixed up
    fish.SetLocation(100, 200);

    // Center of the fish should be a true
    ASSERT_TRUE(fish.HitTest(100, 200));

    // Left of the fish
    ASSERT_FALSE(fish.HitTest(10, 200));

    // Right of the fish
    ASSERT_FALSE(fish.HitTest(200, 200));

    // Above the fish
    ASSERT_FALSE(fish.HitTest(100, 0));

    // Below the fish
    ASSERT_FALSE(fish.HitTest(100, 300));

    // On a fish transparent pixel
    ASSERT_FALSE(fish.HitTest(100 - 125/2 + 17, 200 - 117/2 + 16));
}