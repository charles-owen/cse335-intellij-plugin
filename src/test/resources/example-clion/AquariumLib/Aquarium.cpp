/**
 * @file Aquarium.cpp
 * @author Charles B. Owen
 */
#include "pch.h"

#include "Aquarium.h"
#include "FishBeta.h"

using namespace std;


/**
 * Aquarium Constructor
 */
Aquarium::Aquarium()
{
    mBackground = std::make_unique<wxBitmap>(L"images/background1.png", wxBITMAP_TYPE_ANY);

//    // Loop over the rows of items we will create
//    // We use the constant here to indicate how
//    // many rows we want to create
//    const int NumRows = 5;
//    for (int r = 0; r<NumRows; r++)
//    {
//        // There is a row every 64 pixels and
//        // we start 150 pixels from the top
//        int y = r * 64 + 150;
//
//        // The number of columns starts at 1 and increases as we
//        // go down in the Y direction until half way, then decreases.
//        // If we had 5 rows, the number of columns for each row
//        // will be:  1 2 3 4 1
//        int numCols = NumRows / 2 - abs(r - NumRows / 2) + 1;
//
//        // We center the columns on the screen
//        int xStart = 512 - (numCols - 1) * 128;
//
//        for (int c = 0; c<numCols; c++)
//        {
//            // Each column is 128 pixels to the right.
//            int x = c * 256 + xStart;
//
//            // Create a new fish.
//            // This creates a shared pointer pointing at this fish
//            shared_ptr<Item> fish = make_shared<FishBeta>(this);
//
//            // Set the location
//            fish->SetLocation(x, y);
//
//            // Add to the list of fish.
//            mItems.push_back(fish);
//        }
//    }
}

/**
 * Draw the aquarium
 * @param dc The device context to draw on
 */
void Aquarium::OnDraw(wxDC *dc)
{
    dc->DrawBitmap(*mBackground, 0, 0);

    wxFont font(12, wxFONTFAMILY_SWISS, wxFONTSTYLE_NORMAL, wxFONTWEIGHT_NORMAL);
    dc->SetFont(font);
    dc->SetTextForeground(wxColour(0, 64, 0));
    dc->DrawText(L"Under the Sea!", 10, 10);

    dc->SetFont(wxNullFont);

    for (auto item : mItems)
    {
        item->Draw(dc);
    }
}

/**
 * Add an item to the aquarium
 * @param item New item to add
 */
void Aquarium::Add(std::shared_ptr<Item> item)
{
    mItems.push_back(item);
}

/**
 * Test an x,y click location to see if it clicked
 * on some item in the aquarium.
 * @param x X location in pixels
 * @param y Y location in pixels
 * @returns Pointer to item we clicked on or nullptr if none.
*/
std::shared_ptr<Item> Aquarium::HitTest(int x, int y)
{
    for (auto i = mItems.rbegin(); i != mItems.rend();  i++)
    {
        if ((*i)->HitTest(x, y))
        {
            return *i;
        }
    }

    return  nullptr;
}

/**
 * Move an item to the front of the list of items.
*
* Removes item from the list and adds it to the end so it
* will display last.
* @param item The item to move
*/
void Aquarium::MoveToFront(std::shared_ptr<Item> item)
{
    auto loc = find(begin(mItems), end(mItems), item);
    if (loc != end(mItems))
    {
        mItems.erase(loc);
    }

    mItems.push_back(item);
}

/**
 * We are passed a pointer to a fish that eats. We check to see
 * if there are any fish it is currently over. If so, eat them!
 * @param eater The item that is doing the eating
 * @return true if a fish is eaten
 */
bool Aquarium::Eater(Item *eater)
{
    for(auto other : mItems)
    {
        // Do not compare to ourselves
        if (other.get() == eater) {
            continue;
        }

        if (other->HitTest((int)eater->GetX(), (int)eater->GetY()))
        {
            auto loc = find(begin(mItems), end(mItems), other);
            if (loc != end(mItems))
            {
                mItems.erase(loc);
            }

            return true;
        }

    }
    return false;
}