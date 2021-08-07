/**
 * @file Aquarium.h
 * @author Charles B. Owen
 *
 * The main aquarium class.
 */

#ifndef AQUARIUM_AQUARIUM_H
#define AQUARIUM_AQUARIUM_H

#include <memory>
#include <vector>

class Item;

/**
 * The main aquarium class.
 */
class Aquarium {
public:
    Aquarium();

    void OnDraw(wxDC* graphics);
    void Add(std::shared_ptr<Item> item);
    std::shared_ptr<Item> HitTest(int x, int y);
    void MoveToFront(std::shared_ptr<Item> item);
    bool Eater(Item* eater);

private:
    /// Background image
    std::unique_ptr<wxBitmap> mBackground;

    /// All of the items to populate our aquarium
    std::vector<std::shared_ptr<Item> > mItems;


};

#endif //AQUARIUM_AQUARIUM_H
