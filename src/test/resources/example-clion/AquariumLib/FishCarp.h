/**
 * @file FishCarp.h
 * @author Charles B. Owen
 *
 * Class the implements a Carp fish
 */

#ifndef AQUARIUM_FISHCARP_H
#define AQUARIUM_FISHCARP_H


#include <memory>
#include "Item.h"

/**
 * Class the implements a Carp fish
 */
class FishCarp : public Item {
public:
    /// Default constructor (disabled)
    FishCarp() = delete;

    /// Copy constructor (disabled)
    FishCarp(const FishCarp &) = delete;

    FishCarp(Aquarium* aquarium);

    virtual void Draw(wxDC* dc) override;
    virtual bool HitTest(int x, int y) override;
    virtual void SetLocation(double x, double y) override;

private:
    /// The underlying fish image
    std::unique_ptr<wxImage> mFishImage;

    /// The bitmap we can display for this fish
    std::unique_ptr<wxBitmap> mFishBitmap;


};

#endif //AQUARIUM_FISHCARP_H
