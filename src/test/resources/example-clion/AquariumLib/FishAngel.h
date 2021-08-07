/**
 * @file FishAngel.h
 * @author Charles B. Owen
 *
 * Class the implements a Angel fish
 */

#ifndef AQUARIUM_FISHANGEL_H
#define AQUARIUM_FISHANGEL_H


#include <memory>
#include "Item.h"

/**
 * Class the implements a Angel fish
 */
class FishAngel : public Item {
public:
    /// Default constructor (disabled)
    FishAngel() = delete;

    /// Copy constructor (disabled)
    FishAngel(const FishAngel &) = delete;

    FishAngel(Aquarium* aquarium);

    virtual void Draw(wxDC* dc) override;
    virtual bool HitTest(int x, int y) override;

private:
    /// The underlying fish image
    std::unique_ptr<wxImage> mFishImage;

    /// The bitmap we can display for this fish
    std::unique_ptr<wxBitmap> mFishBitmap;


};

#endif //AQUARIUM_FISHANGEL_H
