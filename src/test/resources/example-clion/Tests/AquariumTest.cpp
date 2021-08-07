#include "gtest/gtest.h"
#include <pch.h>
#include <Aquarium.h>
#include <FishBeta.h>

using namespace std;

TEST(AquariumTest, Construct){
    Aquarium aquarium;
}

TEST(AquariumTest, HitTest) {
    Aquarium aquarium;

    ASSERT_EQ(aquarium.HitTest(100, 200), nullptr) <<
            L"Testing empty aquarium";

    shared_ptr<FishBeta> fish1 = make_shared<FishBeta>(&aquarium);
    fish1->SetLocation(100, 200);
    aquarium.Add(fish1);

    ASSERT_TRUE(aquarium.HitTest(100, 200) == fish1) <<
            L"Testing fish at 100, 200";

    // Test to ensure nullptr if we hit away from any image
    ASSERT_EQ(aquarium.HitTest(0, 200), nullptr) << L"Testing fish at 00, 200";

    // Add two overlapping fish
    shared_ptr<FishBeta> fish2 = make_shared<FishBeta>(&aquarium);
    fish2->SetLocation(400, 400);
    aquarium.Add(fish2);

    shared_ptr<FishBeta> fish3 = make_shared<FishBeta>(&aquarium);
    fish3->SetLocation(400, 400);
    aquarium.Add(fish3);

    // Ensure the top one is found
    ASSERT_EQ(aquarium.HitTest(400, 400), fish3) << L"Testing overlapping fish";
}
