/**
 * @file FishNemo.h
 * @author Charles B. Owen
 *
 * Class the implements a Nemo fish
 */
 
#ifndef AQUARIUM_FISHNEMO_H
#define AQUARIUM_FISHNEMO_H

#include <memory>
#include "Item.h"

/**
 * Class the implements a Nemo fish
 */
class FishNemo : public Item {
public:
    /// Default constructor (disabled)
    FishNemo() = delete;

    /// Copy constructor (disabled)
    FishNemo(const FishNemo &) = delete;

    FishNemo(Aquarium* aquarium);

    virtual void Draw(wxDC* dc) override;
    virtual bool HitTest(int x, int y) override;

private:
    /// The underlying fish image
    std::unique_ptr<wxImage> mFishImage;

    /// The bitmap we can display for this fish
    std::unique_ptr<wxBitmap> mFishBitmap;


};
#endif //AQUARIUM_FISHNEMO_H
