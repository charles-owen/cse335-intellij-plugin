/**
 * @file FishBeta.h
 * @author Charles B. Owen
 *
 * Class the implements a Beta fish
 */

#ifndef AQUARIUM_FISHBETA_H
#define AQUARIUM_FISHBETA_H

#include <memory>
#include "Item.h"

/**
 * Class the implements a Beta fish
 */
class FishBeta : public Item {
public:
    /// Default constructor (disabled)
    FishBeta() = delete;

    /// Copy constructor (disabled)
    FishBeta(const FishBeta &) = delete;

    FishBeta(Aquarium* aquarium);

    virtual void Draw(wxDC* dc) override;
    virtual bool HitTest(int x, int y) override;

private:
    /// The underlying fish image
    std::unique_ptr<wxImage> mFishImage;

    /// The bitmap we can display for this fish
    std::unique_ptr<wxBitmap> mFishBitmap;


};

#endif //AQUARIUM_FISHBETA_H
